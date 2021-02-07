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
    //opens Activity to choose to which SocialMedia to Upload to
    private Button uploadToButton;

    //opens Activity to choose for which SocialMedia you want to add/remove keywords
    private Button subscribeToButton;

    //opens activity to manage both SocialMedias
    private Button manageSocialMediasButton;

    //opens activity to encode/decode/save Files
    private Button stegImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sets JSONPersistentManagerHelper to access App Intern File Storage
        JSONPersistentManager.getInstance().setJsonPersistentHelper(new JSONPersistentWriter(this));

        //jsut to check which SDK is running on the System since it impacts how the App works quite alot
        Log.i("Android SDK", String.valueOf(Build.VERSION.SDK_INT));


        stegImageButton = (Button) findViewById(R.id.stegImageId);
        stegImageButton.setOnClickListener(new View.OnClickListener() {
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