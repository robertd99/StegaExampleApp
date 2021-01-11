package com.example.stegaexampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import de.htw.berlin.steganography.apis.SocialMedia;
import de.htw.berlin.steganography.apis.SocialMediaListener;
import de.htw.berlin.steganography.apis.reddit.Reddit;

public class ManageSocialMediasActivity extends AppCompatActivity {

    Switch redditSwitch;
    TextView redditSearchingONOFF;
    TextView searchResultTextViewReddit;

    List<String> resultMessagesReddit;
    long lastTimeCheckedReddit;
    ImplSocialMediaResults implSocialMediaResults;

    SocialMedia reddit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_social_medias);

        implSocialMediaResults = new ImplSocialMediaResults(this);
        reddit = new Reddit();
        reddit.addAsListener(implSocialMediaResults);
        reddit.subscribeToKeyword("test");

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
        searchResultTextViewReddit = (TextView) findViewById(R.id.manageSocialMediasResultTextViewReddit);






    }

    public void setSearchResultMessage(List<String> message, String socialMediaType) {
        if(socialMediaType.equals("reddit")){
            for(String string : message) {
                resultMessagesReddit.add(string);
            }
        }
        updateResultViews();
    }

    public void setLastTimeChecked(long lastTimeChecked, String socialMediaType){
        if(socialMediaType.equals("reddit")){
            lastTimeCheckedReddit = lastTimeChecked;
        }
        updateResultViews();
    }

    private void updateResultViews() {
        searchResultTextViewReddit.setText("Messages: "+ String.join(", ", resultMessagesReddit) + " Last Time Checked:: "+ lastTimeCheckedReddit);
    }




}