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

    private List<SocialMedia> socialMediaList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_to);
        SocialMediaModel socialMediaModelReddit = new SocialMediaModel();
        SocialMediaModel socialMediaModelImgur = new SocialMediaModel();
        Reddit reddit = new Reddit(socialMediaModelReddit);

        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccountsByType("com.reddit");
        Log.i("halloAccounts","hall accounts");
        Log.i("accounts",String.valueOf(accounts.length));



        socialMediaList.add(new Reddit(socialMediaModelReddit));
        socialMediaList.add(new Imgur(socialMediaModelImgur));
        Log.i("Redditname", String.valueOf(reddit.getApiName()));
        redditButton = (Button) findViewById(R.id.uploadToRedditId);
        redditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOAuthView("reddit","com.example.stegaexampleapp.UploadFileActivity");

            }
        });

        imgurButton = (Button) findViewById(R.id.uploadToImgurId);
        imgurButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOAuthView("imgur","com.example.stegaexampleapp.UploadFileActivity");

            }
        });


    }

    private void openOAuthView(String network, String activityToCall) {
        Intent intent = new Intent(this, OAuthMainActivity.class);
        intent.putExtra("selectedNetwork",network);
        intent.putExtra("activityToCall", activityToCall);
        startActivity(intent);
    }

    private void openUploadFileActivity() {
        Intent intent = new Intent(this, UploadFileActivity.class);
        startActivity(intent);
    }
}