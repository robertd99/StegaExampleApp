package com.example.stegaexampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.htw.berlin.steganography.apis.SocialMedia;

import de.htw.berlin.steganography.apis.imgur.Imgur;

import de.htw.berlin.steganography.apis.SocialMediaModel;

import de.htw.berlin.steganography.apis.models.APINames;
import de.htw.berlin.steganography.apis.reddit.Reddit;
import de.htw.berlin.steganography.persistence.JSONPersistentManager;

public class ManageSocialMediasActivity extends AppCompatActivity {
    //switches for turning SocialMedia search on and off
    Switch redditSwitch, imgurSwitch;
    //textview taht displays either ON or OFF depending on search status
    TextView redditSearchingONOFF, imgurOnOff;
    //results that displays found messages and lastTimeChecked for keyword
    TextView searchResultTextViewReddit, imgurResult;
    //contains all messages found for reddit
    List<String> allResultMessagesReddit = new ArrayList<>();
    //contains all messages found for imgur
    List<String> allResultMessagesImgur = new ArrayList<>();

    long lastTimeCheckedReddit = 0;
    long lastTimeCheckedImgur = 0;

    String lastCheckedKeyword;
    ImplSocialMediaResults implSocialMediaResults;

    SocialMedia reddit, imgur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_social_medias);
        implSocialMediaResults = new ImplSocialMediaResults(this);

        //sets a SocialMediaModel, if you have keywords and lastTimChecked save you can either
        //set them via the SocialMediaModel itself or via the SocialMedia Controller

        SocialMediaModel redditModel = new SocialMediaModel();
        reddit = new Reddit(redditModel);
        reddit = new Reddit(redditModel);
        //puts reddits SocialMediaModel
        reddit.putAllSubscribedKeywordsAndLastTimeChecked(JSONPersistentManager.getInstance().getKeywordAndLastTimeCheckedMapForAPI(APINames.REDDIT));


        //adds implSocialMediaResults as Listener. implSocialMediaResults will get updated on
        //the decoded Messages and when a lastTimeChecked for a specific keyword has changed
        reddit.addAsListener(implSocialMediaResults);
        redditSearchingONOFF = findViewById(R.id.manageSocialMediasRedditSearchingONOFF);
        redditSwitch = findViewById(R.id.manageSocialMediasRedditSwitch);
        searchResultTextViewReddit = (TextView) findViewById(R.id.manageSocialMediasResultTextViewReddit);
        initializeSocialMedia(reddit,redditSwitch,redditSearchingONOFF,searchResultTextViewReddit);

        Log.i("reddit subscribed keywords",String.join(", ", reddit.getAllSubscribedKeywordsAsList()));
        Log.i("reddit last time checked keywords",String.valueOf(reddit.getAllSubscribedKeywordsAndLastTimeChecked().toString()));

        SocialMediaModel imgurModel = new SocialMediaModel();
        imgurModel.putAllSubscribedKeywordsAndLastTimeChecked(JSONPersistentManager.getInstance().getKeywordAndLastTimeCheckedMapForAPI(APINames.IMGUR));
        imgur = new Imgur(imgurModel);
        imgur.subscribeToKeyword("test");
        imgurOnOff = findViewById(R.id.imgurOnOff);
        imgurSwitch = findViewById(R.id.manageSocialMediasImgurSwitch);
        imgurResult = findViewById(R.id.imgurResult);
        initializeSocialMedia(imgur, imgurSwitch, imgurOnOff, imgurResult);




        //scheduler period set to 1 minute. Reddit will search for new results every minute.
        reddit.changeSchedulerPeriod(1);

        //adds new keyword "test" to the SocialMediaModel of reddit. "test" will be initilised with lastTimeChecked 0
        reddit.subscribeToKeyword("test");

        //changes lastTimeChecked of"test" to 20
        reddit.setLastTimeCheckedForKeyword("test",20L);
        reddit.subscribeToKeyword("zero");
        reddit.setLastTimeCheckedForKeyword("zero",400L);




        Log.i("reddit subscribed keywords",String.join(", ", reddit.getAllSubscribedKeywordsAsList()));
        Log.i("reddit last time checked keywords",String.valueOf(reddit.getAllSubscribedKeywordsAndLastTimeChecked().toString()));



    }

   public void initializeSocialMedia(SocialMedia socialMedia, Switch zwitch, TextView tvOnOff, TextView tvResult) {
        //setting switches
        zwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                //stars the search for specified SocialMedia
                socialMedia.startSearch();
                tvOnOff.setText("Searching: ON");
            } else {
                //stops the search for specified SocialMedia
                socialMedia.stopSearch();
                tvOnOff.setText("Searching: OFF");
            }
        });
    }

    //method to change View, adds message to Result String on View
    public void addSearchResultMessages(String socialMediaType, List<String> message) {
        for (String string : message) {
            if (socialMediaType.equals("reddit")) {
                allResultMessagesReddit.add(string);
            } else if (socialMediaType.equals("imgur")) {
                allResultMessagesImgur.add(string);
            }
        }

        if (socialMediaType.equals("reddit")) {
            updateResultViewsReddit();
        } else if (socialMediaType.equals("imgur")) {
            updateResultViewsImgur();
        }
    }

    //method to change View, changes lastTimeChecked for keyword in View
    public void setLastTimeChecked(String socialMediaType, String keyword, long l) {
        lastCheckedKeyword = keyword;

        if (socialMediaType.equals("reddit")) {
            lastTimeCheckedReddit = l;
            updateResultViewsReddit();
        }else if(socialMediaType.equals("imgur")){
            lastTimeCheckedImgur = l;
            updateResultViewsImgur();
        }
    }

    private void updateResultViewsReddit() {
        runOnUiThread(() ->
                searchResultTextViewReddit.setText(String.join(", ", allResultMessagesReddit)
                        + "\n Last Time Checked: " + lastTimeCheckedReddit
                        + "\n for keyword: " + lastCheckedKeyword));
    }

    private void updateResultViewsImgur() {
        runOnUiThread(() ->
                imgurResult.setText(String.join(", ", allResultMessagesImgur)
                        + "\n Last Time Checked: " + lastTimeCheckedImgur
                        + "\n for keyword: " + lastCheckedKeyword));
    }
}