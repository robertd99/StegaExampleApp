package com.example.stegaexampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import de.htw.berlin.steganography.apis.SocialMedia;
import de.htw.berlin.steganography.apis.SocialMediaModel;
import de.htw.berlin.steganography.apis.imgur.Imgur;
import de.htw.berlin.steganography.apis.models.APINames;
import de.htw.berlin.steganography.apis.reddit.Reddit;
import de.htw.berlin.steganography.persistence.JSONPersistentManager;

public class AddRemoveKeywordActivity extends AppCompatActivity {

    String selectedNetworkString;
    TextView selectedSocialMedia;
    TextView currentSubscribedKeywords;
    EditText writtenKeyword;
    Button addKeywordBtn;
    Button removeKeywordBtn;
    SocialMedia socialMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_keyword);

        //retrieves the selectedNetwork to decide for which SocialMedia to call this Activity
        selectedNetworkString = getIntent().getStringExtra("selectedNetwork");
        if(selectedNetworkString.equals("reddit")){
            socialMedia = new Reddit(new SocialMediaModel());
        }
        if(selectedNetworkString.equals("imgur")){
            socialMedia = new Imgur(new SocialMediaModel());
        }
        //retrieves entire Map saved in JSONPersistentManager and puts it into the SocialMediaModel via SocialMedia Controller
        socialMedia.putAllSubscribedKeywordsAndLastTimeChecked(JSONPersistentManager.getInstance().getKeywordAndLastTimeCheckedMapForAPI(APINames.valueOf(selectedNetworkString.toUpperCase())));


        //display of selectedNetwork name
        selectedSocialMedia = (TextView) findViewById(R.id.subscribeSocialMediaTypeId);
        selectedSocialMedia.setText(selectedNetworkString);
        //display of subscirbed Keywords
        currentSubscribedKeywords = (TextView) findViewById(R.id.showCurrentSubscribedKeywordsId);
        currentSubscribedKeywords.setText(listToString(socialMedia.getAllSubscribedKeywordsAsList()));
        writtenKeyword = (EditText) findViewById(R.id.addKeywordId);
        addKeywordBtn = (Button) findViewById(R.id.addKeywordButtonId);
        addKeywordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String keyword = writtenKeyword.getText().toString();
                    //scubscirbing a new Keyword to a SocialMedia
                    socialMedia.subscribeToKeyword(keyword);
                    //subsciribing the keyword to a instance of SocialMedia doesn't persist the Keyword
                    //thats why, if you want to persist it, have to call the JSONPersistentManager seperately with the
                    //same keyword (improvement of AAR would be to set a flag or set a JSONPersisentManager in construcotr of
                    //SocialMedia to do that automatically)
                    JSONPersistentManager.getInstance().addKeywordForApi(APINames.valueOf(selectedNetworkString.toUpperCase()),keyword);


                    currentSubscribedKeywords.setText(listToString(socialMedia.getAllSubscribedKeywordsAsList()));
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        removeKeywordBtn = (Button) findViewById(R.id.removeKeywordButtonId);
        removeKeywordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = writtenKeyword.getText().toString();
                JSONPersistentManager.getInstance().removeKeywordForAPI(APINames.valueOf(selectedNetworkString.toUpperCase()),keyword);
                socialMedia.unsubscribeKeyword(keyword);
                currentSubscribedKeywords.setText(listToString(socialMedia.getAllSubscribedKeywordsAsList()));

            }
        });




    }

    private String listToString(List<String> list){
        String returnString = new String();
        for(String string: list){
            returnString += "; "+string;
        }
        return returnString;
    }
}