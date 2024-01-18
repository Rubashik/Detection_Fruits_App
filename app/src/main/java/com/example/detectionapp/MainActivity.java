package com.example.detectionapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.detectionapp.Classifier.ClassifierActivity;

public class MainActivity extends AppCompatActivity {

    Button classify_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        classify_btn = findViewById(R.id.classify_btn);

    }

    @Override
    protected void onStart() {
        super.onStart();

        classify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent classifyIntent = new Intent(MainActivity.this, ClassifierActivity.class);
                startActivity(classifyIntent);
            }
        });
    }
}
