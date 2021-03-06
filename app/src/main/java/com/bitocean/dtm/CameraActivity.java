package com.bitocean.dtm;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CameraActivity extends BaseTimerActivity {

    // public CameraPreview mPreview;
    public static boolean isCameraPicture = false;
    public Button submit, photo, cancel;
    public TextView timer;
    public static String fileType;
    private Camera camera;
    private static byte[] data;
    private Camera.Parameters parameters = null;
    private static String path;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (getIntent() != null) {
            fileType = getIntent().getStringExtra("fileType");
        }

        setContentView(R.layout.activity_camera);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    saveToSDCard(CameraActivity.data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent data = new Intent();
                data.putExtra("url", path);
                CameraActivity.this.setResult(RESULT_OK, data);
                finish();
            }

        });
        photo = (Button) findViewById(R.id.photo);
        photo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                isCameraPicture = true;
                camera.autoFocus(new AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        // TODO Auto-generated method stub
                        camera.takePicture(null, null, new MyPictureCallback());
                    }
                });
            }

        });
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (isCameraPicture) {
                    photo.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.GONE);
                    camera.startPreview();
                    isCameraPicture = false;
                } else {
                    CameraActivity.this.setResult(
                            CameraActivity.this.RESULT_CANCELED, null);
                    finish();
                }
            }

        });

        SurfaceView surfaceView = (SurfaceView) this
                .findViewById(R.id.surfaceView);
        surfaceView.getHolder()
                .setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(new SurfaceCallback());
    }

    private final class MyPictureCallback implements PictureCallback {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                CameraActivity.data = data;
                photo.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                camera.stopPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveToSDCard(byte[] data) throws IOException {
        path = "/sdcard/" + fileType
                + new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date()) + ".jpg";
        File file = new File(path);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(data);
        outputStream.close();
    }

    private final class SurfaceCallback implements Callback {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            parameters.setPreviewSize(1920, 1080); //获得摄像区域的大小
            parameters.set("jpeg-quality", 85);//设置照片质量
            parameters.setPictureSize(1920, 1080);//设置拍出来的屏幕大小
            camera.setParameters(parameters);//把上面的设置 赋给摄像头
            try {
                camera.setPreviewDisplay(holder);//把摄像头获得画面显示在SurfaceView控件里面
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();//开始预览

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera = Camera.open();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                camera.release();
                camera = null;
            }
        }
    }
}