package com.example.detectionapp.Recognizer;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.example.detectionapp.R;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class RecognizerActivity extends CameraActivity{

    private Mat mRgba;
    private Mat mGray;
    private CameraBridgeViewBase cameraBridgeViewBase;
    private ObjectDetectorClass objectDetectorClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognizer);

        getPermission();

        cameraBridgeViewBase = findViewById(R.id.cameraView);

        cameraBridgeViewBase.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {
                mRgba=new Mat(height,width, CvType.CV_8UC4);
                mGray =new Mat(height,width,CvType.CV_8UC1);
            }

            @Override
            public void onCameraViewStopped() {
                mRgba.release();
            }

            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
                mRgba=inputFrame.rgba();
                mGray=inputFrame.gray();
                // Before watching this video please watch previous video of loading tensorflow lite model

                // now call that function
                Mat out=new Mat();
                out=objectDetectorClass.recognizeImage(mRgba);

                return out;
            }
        });
        if(OpenCVLoader.initDebug()){
            cameraBridgeViewBase.enableView();
        }

        try {
            objectDetectorClass = new ObjectDetectorClass(getAssets(), "RecognitionModel.tflite", 320);
            Log.d("MainActivity", "Model successfully loaded");
        } catch (IOException e) {
            Log.d("MainActivity", "Model getting some error");
            e.printStackTrace();
        }
    }

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(cameraBridgeViewBase);
    }

    void getPermission(){
        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
        }
    }
}