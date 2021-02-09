package com.example.stegaexampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import de.htw.berlin.steganography.apis.SocialMedia;
import de.htw.berlin.steganography.apis.SocialMediaModel;
import de.htw.berlin.steganography.apis.imgur.Imgur;
import de.htw.berlin.steganography.apis.reddit.Reddit;
import de.htw.berlin.steganography.OAuthMainActivity;

public class UploadToActivity extends AppCompatActivity {
    private Button redditButton;
    private Button imgurButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_to);

        //calls openOAuthActivity with "reddit" and "com.example.stegaexampleapp.UploadFileActivity"
        redditButton = (Button) findViewById(R.id.uploadToRedditId);
        redditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOAuthView("reddit","com.example.stegaexampleapp.UploadFileActivity");

            }
        });
        //calls openOAuthActivity with "imgur" and "com.example.stegaexampleapp.UploadFileActivity"
        imgurButton = (Button) findViewById(R.id.uploadToImgurId);
        imgurButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOAuthView("imgur","com.example.stegaexampleapp.UploadFileActivity");

            }
        });


    }

    //calls OAuthMainActivity out of the Steganography AAR for the network, reflective access actvitiyToCall so the
    //OAuthMainActivity knows which activity to start after successfull OAuth2 Flow.
    //The AccesToken will be send as an Extra under the Key "accesToken".
    //The Network name it was called for under the Extra Key "selectedNetwork"
    private void openOAuthView(String network, String activityToCall) {
        Intent intent = new Intent(this, OAuthMainActivity.class);
        intent.putExtra("selectedNetwork",network);
        intent.putExtra("activityToCall", activityToCall);
        startActivity(intent);
    }

}