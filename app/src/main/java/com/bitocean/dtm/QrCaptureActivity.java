package com.bitocean.dtm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.*;
import android.os.Process;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.bitocean.dtm.util.Util;
import com.bitocean.dtm.zxing.CameraManager;
import com.bitocean.dtm.zxing.ScannerView;
import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public final class QrCaptureActivity extends BaseTimerActivity implements SurfaceHolder.Callback {
    private static final long AUTO_FOCUS_INTERVAL_MS = 2500L;

    private final CameraManager cameraManager = new CameraManager();
    private ScannerView scannerView;
    private SurfaceHolder surfaceHolder;
    private HandlerThread cameraThread;
    private Handler cameraHandler;

    private static final int DIALOG_CAMERA_PROBLEM = 0;
    private static boolean DISABLE_CONTINUOUS_AUTOFOCUS = Build.MODEL.equals("GT-I9100") // Galaxy S2
            || Build.MODEL.equals("SGH-T989") // Galaxy S2
            || Build.MODEL.equals("SGH-T989D") // Galaxy S2 X
            || Build.MODEL.equals("SAMSUNG-SGH-I727") // Galaxy S2 Skyrocket
            || Build.MODEL.equals("GT-I9300") // Galaxy S3
            || Build.MODEL.equals("GT-N7000"); // Galaxy Note

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        scannerView = (ScannerView) findViewById(R.id.scan_activity_mask);
        Button btn_cancel = (Button) findViewById(R.id.cancel_scan);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraThread = new HandlerThread("cameraThread", Process.THREAD_PRIORITY_BACKGROUND);
        cameraThread.start();
        cameraHandler = new Handler(cameraThread.getLooper());

        final SurfaceView surfaceView = (SurfaceView) findViewById(R.id.scan_activity_preview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(final SurfaceHolder holder) {
        cameraHandler.post(openRunnable);
    }

    @Override
    public void surfaceDestroyed(final SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(final SurfaceHolder holder, final int format, final int width, final int height) {
    }

    @Override
    protected void onPause() {
        cameraHandler.post(closeRunnable);
        surfaceHolder.removeCallback(this);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_FOCUS:
            case KeyEvent.KEYCODE_CAMERA:
                // don't launch camera app
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_VOLUME_UP:
                cameraHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        cameraManager.setTorch(keyCode == KeyEvent.KEYCODE_VOLUME_UP);
                    }
                });
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void handleResult(final Result scanResult, final Bitmap thumbnailImage) {
        // superimpose dots to highlight the key features of the qr code
        String resultString = scanResult.getText();
        if (resultString.equals("")) {
            new Util(this).showFeatureToast(R.string.scan_error);
        } else {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("result", resultString);
            bundle.putParcelable("bitmap", thumbnailImage);
            resultIntent.putExtras(bundle);
            setResult(RESULT_OK, resultIntent);
        }
        // delayed finish
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    private final Runnable openRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                final Camera camera = cameraManager.open(surfaceHolder, DISABLE_CONTINUOUS_AUTOFOCUS);
                final Rect framingRect = cameraManager.getFrame();
                final Rect framingRectInPreview = cameraManager.getFramePreview();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scannerView.setFraming(framingRect, framingRectInPreview);
                    }
                });
                final String focusMode = camera.getParameters().getFocusMode();
                final boolean nonContinuousAutoFocus = Camera.Parameters.FOCUS_MODE_AUTO.equals(focusMode)
                        || Camera.Parameters.FOCUS_MODE_MACRO.equals(focusMode);
                //if (nonContinuousAutoFocus)
                cameraHandler.post(new AutoFocusRunnable(camera));
                cameraHandler.post(fetchAndDecodeRunnable);
            } catch (final IOException x) {
                showDialog(DIALOG_CAMERA_PROBLEM);
            } catch (final RuntimeException x) {
                showDialog(DIALOG_CAMERA_PROBLEM);
            }
        }
    };

    private final Runnable closeRunnable = new Runnable() {
        @Override
        public void run() {
            cameraManager.close();
            // cancel background thread
            cameraHandler.removeCallbacksAndMessages(null);
            cameraThread.quit();
        }
    };

    private final class AutoFocusRunnable implements Runnable {
        private final Camera camera;
        public AutoFocusRunnable(final Camera camera) {
            this.camera = camera;
        }
        @Override
        public void run() {
            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(final boolean success, final Camera camera) {
                    // schedule again
                    cameraHandler.postDelayed(AutoFocusRunnable.this, AUTO_FOCUS_INTERVAL_MS);
                }
            });
        }
    }

    private final Runnable fetchAndDecodeRunnable = new Runnable() {
        private final QRCodeReader reader = new QRCodeReader();
        private final Map<DecodeHintType, Object> hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
        @Override
        public void run() {
            cameraManager.requestPreviewFrame(new PreviewCallback() {
                @Override
                public void onPreviewFrame(final byte[] data, final Camera camera) {
                    decode(data);
                }
            });
        }

        private void decode(final byte[] data) {
            final PlanarYUVLuminanceSource source = cameraManager.buildLuminanceSource(data);
            final BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            try {
                hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, new ResultPointCallback() {
                    @Override
                    public void foundPossibleResultPoint(final ResultPoint dot) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                scannerView.addDot(dot);
                            }
                        });
                    }
                });
                final Result scanResult = reader.decode(bitmap, hints);
                final int thumbnailWidth = source.getThumbnailWidth();
                final int thumbnailHeight = source.getThumbnailHeight();
                final Bitmap thumbnailImage = Bitmap.createBitmap(thumbnailWidth, thumbnailHeight, Bitmap.Config.ARGB_8888);
                thumbnailImage.setPixels(source.renderThumbnail(), 0, thumbnailWidth, 0, 0, thumbnailWidth, thumbnailHeight);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleResult(scanResult, thumbnailImage);
                    }
                });
            } catch (final ReaderException x) {
                // retry
                cameraHandler.post(fetchAndDecodeRunnable);
            } finally {
                reader.reset();
            }
        }
    };
}