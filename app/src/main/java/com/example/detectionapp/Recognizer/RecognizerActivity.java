package com.example.detectionapp.Recognizer;


import androidx.annotation.Nullable;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.example.detectionapp.R;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class RecognizerActivity extends CameraActivity implements TextToSpeech.OnInitListener{

    private Mat mRgba;
    private Mat mGray;
    private CameraBridgeViewBase cameraBridgeViewBase;
    private ObjectDetectorClass objectDetectorClass;
    private TextToSpeech mTextToSpeech;
    private boolean mIsInit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognizer);

        getPermission();

        cameraBridgeViewBase = findViewById(R.id.cameraView);

        mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    Locale locale = new Locale("en");
                    int result = mTextToSpeech.setLanguage(locale);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        mIsInit = false;
                    } else {
                        mIsInit = true;
                        String textToSpeech = "You opened the recognizer! Now you can point the camera at an object for recognition.";
                        mTextToSpeech.speak(textToSpeech, TextToSpeech.QUEUE_FLUSH, null, "id1");
                    }
                } else {
                    Log.e("TextToSpeech", "Initialization failed");
                    mIsInit = false;
                }
            }
        });

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

                mTextToSpeech.speak(objectDetectorClass.getPredictedObject(), TextToSpeech.QUEUE_FLUSH, null, "id1");

                return out;
            }
        });
        if(OpenCVLoader.initDebug()){
            cameraBridgeViewBase.enableView();
        }

        try {
            objectDetectorClass = new ObjectDetectorClass(getAssets(), "Recognition_model.tflite", 320);
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
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Locale locale = new Locale("en");
            int result = mTextToSpeech.setLanguage(locale);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                mIsInit = false;
            } else {
                mIsInit = true;
            }
        } else {
            Log.e("TextToSpeech", "Initialization failed");
            mIsInit = false;
        }
    }
}