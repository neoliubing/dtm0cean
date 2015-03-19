
package com.bitocean.dtm.zxing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PreviewCallback;
import android.view.SurfaceHolder;

import com.google.zxing.PlanarYUVLuminanceSource;

/**
 * @author Andreas Schildbach
 */
public final class CameraManager {
    private static final int MIN_FRAME_SIZE = 240;
    private static final int MAX_FRAME_SIZE = 600;
    private static final int MIN_PREVIEW_PIXELS = 470 * 320; // normal screen
    private static final int MAX_PREVIEW_PIXELS = 1280 * 720;

    private Camera camera = null;
    private Camera.Size cameraResolution;
    private Rect frame;
    private Rect framePreview;

    public Rect getFrame() {
        return frame;
    }

    public Rect getFramePreview() {
        return framePreview;
    }

    public Camera open(final SurfaceHolder holder, final boolean continuousAutoFocus) throws IOException {
//        camera = Camera.open();
        camera = null;
        if (camera == null) {
            final int cameraCount = Camera.getNumberOfCameras();
            final CameraInfo cameraInfo = new CameraInfo();
            // search for front-facing camera
            for (int i = 0; i < cameraCount; i++) {
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                    camera = Camera.open(i);
                    break;
                }
            }
        }

        camera.setPreviewDisplay(holder);

        final Camera.Parameters parameters = camera.getParameters();

        final Rect surfaceFrame = holder.getSurfaceFrame();
        cameraResolution = findBestPreviewSizeValue(parameters, surfaceFrame);

        final int surfaceWidth = surfaceFrame.width();
        final int surfaceHeight = surfaceFrame.height();

//        final int rawSize = Math.min(surfaceWidth * 2 / 3, surfaceHeight * 2 / 3);
        final int frameSize = 900;//= Math.max(MIN_FRAME_SIZE, Math.min(MAX_FRAME_SIZE, rawSize));

        final int leftOffset = (surfaceWidth - frameSize) / 2;
        final int topOffset = (surfaceHeight - frameSize) / 2;
        frame = new Rect(leftOffset, topOffset, leftOffset + frameSize, topOffset + frameSize);
        framePreview = new Rect(frame.left * cameraResolution.width / surfaceWidth, frame.top * cameraResolution.height / surfaceHeight, frame.right
                * cameraResolution.width / surfaceWidth, frame.bottom * cameraResolution.height / surfaceHeight);

        final String savedParameters = parameters == null ? null : parameters.flatten();

        try {
            setDesiredCameraParameters(camera, cameraResolution, continuousAutoFocus);
        } catch (final RuntimeException x) {
            if (savedParameters != null) {
                final Camera.Parameters parameters2 = camera.getParameters();
                parameters2.unflatten(savedParameters);
                try {
                    camera.setParameters(parameters2);
                    setDesiredCameraParameters(camera, cameraResolution, continuousAutoFocus);
                } catch (final RuntimeException x2) {
                }
            }
        }
//        setZoom();
        camera.startPreview();
        return camera;
    }

//    public boolean isSupportZoom() {
//        boolean isSuppport = true;
//        if (camera.getParameters().isSmoothZoomSupported()) {
//            isSuppport = false;
//        }
//        return isSuppport;
//    }
//
//    public void setZoom() {
//        if (isSupportZoom()) {
//            try {
//                Camera.Parameters params = camera.getParameters();
//                final int MAX = params.getMaxZoom();
//                if (MAX == 0)
//                    return;
//
//                int zoomValue = params.getZoom();
//
//                zoomValue += 50;
//                params.setZoom(zoomValue);
//                camera.setParameters(params);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//
//        }
//    }

    public void close() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    private static final Comparator<Camera.Size> numPixelComparator = new Comparator<Camera.Size>() {
        @Override
        public int compare(final Camera.Size size1, final Camera.Size size2) {
            final int pixels1 = size1.height * size1.width;
            final int pixels2 = size2.height * size2.width;

            if (pixels1 < pixels2)
                return 1;
            else if (pixels1 > pixels2)
                return -1;
            else
                return 0;
        }
    };

    private static Camera.Size findBestPreviewSizeValue(final Camera.Parameters parameters, Rect surfaceResolution) {


        if (surfaceResolution.height() > surfaceResolution.width())
            surfaceResolution = new Rect(0, 0, surfaceResolution.height(), surfaceResolution.width());

        final float screenAspectRatio = (float) surfaceResolution.width() / (float) surfaceResolution.height();

        final List<Camera.Size> rawSupportedSizes = parameters.getSupportedPreviewSizes();
        if (rawSupportedSizes == null)
            return parameters.getPreviewSize();

        // sort by size, descending
        final List<Camera.Size> supportedPreviewSizes = new ArrayList<Camera.Size>(rawSupportedSizes);
        Collections.sort(supportedPreviewSizes, numPixelComparator);

        Camera.Size bestSize = null;
        float diff = Float.POSITIVE_INFINITY;

        for (final Camera.Size supportedPreviewSize : supportedPreviewSizes) {
            final int realWidth = supportedPreviewSize.width;
            final int realHeight = supportedPreviewSize.height;
            final int realPixels = realWidth * realHeight;
            if (realPixels < MIN_PREVIEW_PIXELS || realPixels > MAX_PREVIEW_PIXELS)
                continue;

            final boolean isCandidatePortrait = realWidth < realHeight;
            final int maybeFlippedWidth = isCandidatePortrait ? realHeight : realWidth;
            final int maybeFlippedHeight = isCandidatePortrait ? realWidth : realHeight;
            if (maybeFlippedWidth == surfaceResolution.width() && maybeFlippedHeight == surfaceResolution.height())
                return supportedPreviewSize;

            final float aspectRatio = (float) maybeFlippedWidth / (float) maybeFlippedHeight;
            final float newDiff = Math.abs(aspectRatio - screenAspectRatio);
            if (newDiff < diff) {
                bestSize = supportedPreviewSize;
                diff = newDiff;
            }
        }

        if (bestSize != null)
            return bestSize;
        else
            return parameters.getPreviewSize();
    }

    private static void setDesiredCameraParameters(final Camera camera, final Camera.Size cameraResolution, final boolean continuousAutoFocus) {
        final Camera.Parameters parameters = camera.getParameters();
        if (parameters == null)
            return;

        final List<String> supportedFocusModes = parameters.getSupportedFocusModes();
        final String focusMode = continuousAutoFocus ? findValue(supportedFocusModes, Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE,
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO, Camera.Parameters.FOCUS_MODE_AUTO, Camera.Parameters.FOCUS_MODE_MACRO) : findValue(
                supportedFocusModes, Camera.Parameters.FOCUS_MODE_AUTO, Camera.Parameters.FOCUS_MODE_MACRO);
        if (focusMode != null)
            parameters.setFocusMode(focusMode);

        parameters.setPreviewSize(cameraResolution.width, cameraResolution.height);

        camera.setParameters(parameters);
    }

    public void requestPreviewFrame(final PreviewCallback callback) {
        camera.setOneShotPreviewCallback(callback);
    }

    public PlanarYUVLuminanceSource buildLuminanceSource(final byte[] data) {
        return new PlanarYUVLuminanceSource(data, cameraResolution.width, cameraResolution.height, framePreview.left, framePreview.top,
                framePreview.width(), framePreview.height(), false);
    }

    public void setTorch(final boolean enabled) {
        if (enabled != getTorchEnabled(camera))
            setTorchEnabled(camera, enabled);
    }

    private static boolean getTorchEnabled(final Camera camera) {
        final Camera.Parameters parameters = camera.getParameters();
        if (parameters != null) {
            final String flashMode = camera.getParameters().getFlashMode();
            return flashMode != null && (Camera.Parameters.FLASH_MODE_ON.equals(flashMode) || Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode));
        }

        return false;
    }

    private static void setTorchEnabled(final Camera camera, final boolean enabled) {
        final Camera.Parameters parameters = camera.getParameters();

        final List<String> supportedFlashModes = parameters.getSupportedFlashModes();
        if (supportedFlashModes != null) {
            final String flashMode;
            if (enabled)
                flashMode = findValue(supportedFlashModes, Camera.Parameters.FLASH_MODE_TORCH, Camera.Parameters.FLASH_MODE_ON);
            else
                flashMode = findValue(supportedFlashModes, Camera.Parameters.FLASH_MODE_OFF);

            if (flashMode != null) {
                camera.cancelAutoFocus(); // autofocus can cause conflict

                parameters.setFlashMode(flashMode);
                camera.setParameters(parameters);
            }
        }
    }

    private static String findValue(final Collection<String> values, final String... valuesToFind) {
        for (final String valueToFind : valuesToFind)
            if (values.contains(valueToFind))
                return valueToFind;

        return null;
    }
}
