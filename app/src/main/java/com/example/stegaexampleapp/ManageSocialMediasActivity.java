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
import de.htw.berlin.steganography.apis.models.APINames;
import de.htw.berlin.steganography.apis.reddit.Reddit;
import de.htw.berlin.steganography.persistence.JSONPersistentManager;

public class ManageSocialMediasActivity extends AppCompatActivity {

    Switch redditSwitch, imgurSwitch;
    TextView redditSearchingONOFF, imgurOnOff;
    TextView searchResultTextViewReddit, imgurResult;

    List<String> allResultMessagesReddit = new ArrayList<>();
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

        reddit = new Reddit();
        redditSearchingONOFF = findViewById(R.id.manageSocialMediasRedditSearchingONOFF);
        redditSwitch = findViewById(R.id.manageSocialMediasRedditSwitch);
        searchResultTextViewReddit = (TextView) findViewById(R.id.manageSocialMediasResultTextViewReddit);
        initializeSocialMedia(reddit, redditSwitch, redditSearchingONOFF, searchResultTextViewReddit);

        imgur = new Imgur();
        imgurOnOff = findViewById(R.id.imgurOnOff);
        imgurSwitch = findViewById(R.id.manageSocialMediasImgurSwitch);
        imgurResult = findViewById(R.id.imgurResult);
        initializeSocialMedia(imgur, imgurSwitch, imgurOnOff, imgurResult);
    }

    public void initializeSocialMedia(SocialMedia socialMedia, Switch zwitch, TextView tvOnOff, TextView tvResult) {
        Log.i("Init", socialMedia.getApiName() + " gets initialized... ");

        socialMedia.addAsListener(implSocialMediaResults);
        socialMedia.putAllSubscribedKeywordsAndLastTimeChecked(JSONPersistentManager.getInstance().getKeywordAndLastTimeCheckedMapForAPI(APINames.REDDIT));
        socialMedia.subscribeToKeyword("na");
        socialMedia.getLastTimeCheckedForKeyword("na");
        socialMedia.changeSchedulerPeriod(1);

        zwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                socialMedia.startSearch();
                tvOnOff.setText("Searching: ON");
            } else {
                socialMedia.stopSearch();
                tvOnOff.setText("Searching: OFF");
            }
        });
    }

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