package com.example.detectionapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.detectionapp.Classifier.ClassifierActivity;
import com.example.detectionapp.Detector.DetectorActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private TextToSpeech mTextToSpeech;
    private boolean mIsInit;
    Button classify_btn, recognize_btn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        String textToSpeech = "Main Page. If you want to classify image press the left side of screen. If you want to recognize - press the right side.";
                        mTextToSpeech.speak(textToSpeech, TextToSpeech.QUEUE_FLUSH, null, "id1");
                    }
                } else {
                    Log.e("TextToSpeech", "Initialization failed");
                    mIsInit = false;
                }
            }
        });
        classify_btn = findViewById(R.id.classify_btn);
        recognize_btn = findViewById(R.id.recognize_btn_btn);

    }

    @Override
    protected void onStart() {
        super.onStart();

        recognize_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recognizerIntent = new Intent(MainActivity.this, DetectorActivity.class);
                startActivity(recognizerIntent);
            }
        });
        classify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent classifyIntent = new Intent(MainActivity.this, ClassifierActivity.class);
                startActivity(classifyIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                        String textToSpeech = "Main Page. If you want to classify image press the left side of screen. If you want to recognize - press the right side.";
                        mTextToSpeech.speak(textToSpeech, TextToSpeech.QUEUE_FLUSH, null, "id1");
                    }
                } else {
                    Log.e("TextToSpeech", "Initialization failed");
                    mIsInit = false;
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

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
                        String textToSpeech = "Main Page. If you want to classify image press the left side of screen. If you want to recognize - press the right side.";
                        mTextToSpeech.speak(textToSpeech, TextToSpeech.QUEUE_FLUSH, null, "id1");
                    }
                } else {
                    Log.e("TextToSpeech", "Initialization failed");
                    mIsInit = false;
                }
            }
        });
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
