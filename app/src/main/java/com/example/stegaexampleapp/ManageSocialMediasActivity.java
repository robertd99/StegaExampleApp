package com.example.stegaexampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.htw.berlin.steganography.apis.SocialMedia;
import de.htw.berlin.steganography.apis.models.APINames;
import de.htw.berlin.steganography.apis.reddit.Reddit;
import de.htw.berlin.steganography.persistence.JSONPersistentManager;

public class ManageSocialMediasActivity extends AppCompatActivity {

    Switch redditSwitch;
    TextView redditSearchingONOFF;
    TextView searchResultTextViewReddit;

    List<String> allResultMessagesReddit = new ArrayList<>();
    long lastTimeCheckedReddit = 0;
    ImplSocialMediaResults implSocialMediaResults;

    SocialMedia reddit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_social_medias);

        searchResultTextViewReddit = (TextView) findViewById(R.id.manageSocialMediasResultTextViewReddit);


        implSocialMediaResults = new ImplSocialMediaResults(this);
        reddit = new Reddit();
        reddit.addAsListener(implSocialMediaResults);
        //reddit.setLastTimeChecked(JSONPersistentManager.getInstance().getLastTimeCheckedForAPI(APINames.REDDIT));
        reddit.setLastTimeChecked(0);
        String keyword = "test";
        reddit.subscribeToKeyword(keyword);
        JSONPersistentManager.getInstance().addKeywordForAPI(APINames.REDDIT, keyword);
        reddit.setAllSubscribedKeywords(JSONPersistentManager.getInstance().getKeywordListForAPI(APINames.REDDIT));
        Log.i("reddit subscribed keywords",String.join(", ", reddit.getAllSubscribedKeywords()));
        Log.i("reddit last time checked keywords",String.valueOf(reddit.getLastTimeChecked()));

        redditSearchingONOFF = findViewById(R.id.manageSocialMediasRedditSearchingONOFF);
        redditSwitch = findViewById(R.id.manageSocialMediasRedditSwitch);
        redditSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    reddit.startSearch();
                    redditSearchingONOFF.setText("Searching: ON");
                }
                else{
                    reddit.stopSearch();
                    redditSearchingONOFF.setText("Searching: OFF");
                }
            }
        });






    }

    public void addSearchResultMessages(List<String> message, String socialMediaType) {
        if(socialMediaType.equals("reddit")){
            for(String string : message) {
                allResultMessagesReddit.add(string);
            }
        }
        updateResultViewsReddit();
    }

    public void setLastTimeChecked(long lastTimeChecked, String socialMediaType){
        if(socialMediaType.equals("reddit")){
            lastTimeCheckedReddit = lastTimeChecked;
        }
        updateResultViewsReddit();


    }

    private void updateResultViewsReddit() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                searchResultTextViewReddit.setText(String.join(", ", allResultMessagesReddit) + "\n Last Time Checked: "+ lastTimeCheckedReddit);


            }
        });
        }




}