package com.example.stegaexampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import java.util.HashMap;
import java.util.Map;

import de.htw.berlin.steganography.persistence.JSONPersistentWriter;
import de.htw.berlin.steganography.persistence.JSONPersistentManager;

public class MainActivity extends AppCompatActivity {
    private Button uploadToButton;
    private Button subscribeToButton;
    private Button manageSocialMediasButton;
    private Button stegImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JSONPersistentManager.getInstance().setJsonPersistentHelper(new JSONPersistentWriter(this));

        Log.i("Android SDK", String.valueOf(Build.VERSION.SDK_INT));
        Map<String, Long> myMap = new HashMap<>();
        myMap.put("hi", new Long(0));

        uploadToButton = (Button) findViewById(R.id.stegImageId);
        uploadToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStegImageActivity();
            }


        });

        uploadToButton = (Button) findViewById(R.id.uploadToId);
        uploadToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUploadToActivity();
            }


        });

        subscribeToButton = (Button) findViewById(R.id.subscribeToId);
        subscribeToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSubscribeToActivity();
            }
        });

        manageSocialMediasButton = (Button) findViewById(R.id.manageSocialMediasId);
        manageSocialMediasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openManageSociaMediasActivity();
            }
        });


    }

    private void openStegImageActivity() {
        Intent intent = new Intent(this , StegImageActivity.class);
        startActivity(intent);
    }

    private void openManageSociaMediasActivity() {
        Intent intent = new Intent(this , ManageSocialMediasActivity.class);
        startActivity(intent);
    }


    private void openSubscribeToActivity() {
        Intent intent = new Intent(this , SubscribeToActivity.class);
        startActivity(intent);
    }

    private void openUploadToActivity() {
        Intent intent = new Intent(this , UploadToActivity.class);
        startActivity(intent);
    }
}