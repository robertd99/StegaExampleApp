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
    String lastCheckedKeyword;
    ImplSocialMediaResults implSocialMediaResults;

    SocialMedia reddit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_social_medias);

        searchResultTextViewReddit = (TextView) findViewById(R.id.manageSocialMediasResultTextViewReddit);


        implSocialMediaResults = new ImplSocialMediaResults(this);
        reddit = new Reddit();
        String keyword = "test";
        reddit.addAsListener(implSocialMediaResults);

        reddit.putAllSubscribedKeywordsAndLastTimeChecked(JSONPersistentManager.getInstance().getKeywordAndLastTimeCheckedMapForAPI(APINames.REDDIT));
        //reddit.changeSchedulerPeriod(1);
        reddit.subscribeToKeyword(keyword);
        reddit.subscribeToKeyword("hallo");
        reddit.getLastTimeCheckedForKeyword("hallo");
        //reddit.setLastTimeCheckedForKeyword(keyword, 1111L);
        //JSONPersistentManager.getInstance().addKeywordForApi(APINames.REDDIT, keyword);
        //reddit.setAllSubscribedKeywords(JSONPersistentManager.getInstance().getKeywordListForAPI(APINames.REDDIT));
        //reddit.startSearch();

        Log.i("reddit subscribed keywords",String.join(", ", reddit.getAllSubscribedKeywordsAsList()));
        Log.i("reddit last time checked keywords",String.valueOf(reddit.getAllSubscribedKeywordsAndLastTimeChecked().toString()));

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

    public void addSearchResultMessages(String socialMediaType, List<String> message) {
        if(socialMediaType.equals("reddit")){
            for(String string : message) {
                allResultMessagesReddit.add(string);
            }
        }
        updateResultViewsReddit();
    }

    public void setLastTimeChecked(String socialMediaType, String keyword, long l){
        if(socialMediaType.equals("reddit")){
            lastTimeCheckedReddit = l;
            lastCheckedKeyword = keyword;
        }
        updateResultViewsReddit();


    }

    private void updateResultViewsReddit() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                searchResultTextViewReddit.setText(String.join(", ", allResultMessagesReddit) + "\n Last Time Checked: "+ lastTimeCheckedReddit+ "\n for keyword: " + lastCheckedKeyword);


            }
        });
        }




}