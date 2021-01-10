package com.example.stegaexampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import de.htw.berlin.steganography.apis.SocialMediaListener;
import de.htw.berlin.steganography.apis.reddit.Reddit;

public class ManageSocialMediasActivity extends AppCompatActivity {

    TextView searchResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_social_medias);

        searchResultTextView = (TextView) findViewById(R.id.manageSocialMediasTextSearchResultsId);


        ImplSocialMediaResults implSocialMediaResults = new ImplSocialMediaResults(searchResultTextView);
        Reddit reddit = new Reddit();
        reddit.addAsListener(implSocialMediaResults);
        reddit.subscribeToKeyword("test");
        reddit.startSearch();



    }
}