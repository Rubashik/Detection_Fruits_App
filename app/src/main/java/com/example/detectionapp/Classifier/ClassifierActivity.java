package com.example.detectionapp.Classifier;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.detectionapp.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ClassifierActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech mTextToSpeech;
    private boolean mIsInit;
    Button camera, gallery;
    ImageView imageView;
    TextView result, confidencesView;
    int imageSize = 32;
    Classifier classifier = new Classifier();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifier);

        camera = findViewById(R.id.button);
        mTextToSpeech = new TextToSpeech(this, this);
        gallery = findViewById(R.id.button2);

        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);
        confidencesView = findViewById(R.id.confidenceTextView);


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 3){
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                result.setText(classifier.classifyImage(image, getApplicationContext()));
            }else{
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                result.setText(classifier.classifyImage(image, getApplicationContext()));
            }

            HashMap<String, Float> confidencesMap = (HashMap<String, Float>) classifier.getConfidences();
            StringBuilder stringBuilder = new StringBuilder();

            for (Map.Entry<String, Float> entry : confidencesMap.entrySet()) {
                String fruit = entry.getKey();
                float confidence = entry.getValue();
//                String formattedString = String.format("%.1f", confidence*100);

                stringBuilder.append(fruit).append(": ").append(confidence).append("%\n");
                confidencesView.setText(stringBuilder.toString());
            }

            if (mIsInit) {
                String textToSpeech = (String) result.getText();
                mTextToSpeech.speak(textToSpeech, TextToSpeech.QUEUE_FLUSH, null, "id1");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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