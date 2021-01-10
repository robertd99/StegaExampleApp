package com.example.stegaexampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import de.htw.berlin.steganography.apis.SocialMedia;
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


        selectedNetworkString = getIntent().getStringExtra("selectedNetwork");
        if(selectedNetworkString.equals("reddit")){
            socialMedia = new Reddit();
        }
        if(selectedNetworkString.equals("imgur")){
            socialMedia = new Imgur();
        }
        try {
            socialMedia.setAllSubscribedKeywords(JSONPersistentManager.getInstance().getKeywordListForAPI(APINames.valueOf(selectedNetworkString.toUpperCase())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        selectedSocialMedia = (TextView) findViewById(R.id.subscribeSocialMediaTypeId);
        selectedSocialMedia.setText(selectedNetworkString);
        currentSubscribedKeywords = (TextView) findViewById(R.id.showCurrentSubscribedKeywordsId);

        currentSubscribedKeywords.setText(listToString(socialMedia.getAllSubscribedKeywords()));

        writtenKeyword = (EditText) findViewById(R.id.addKeywordId);

        addKeywordBtn = (Button) findViewById(R.id.addKeywordButtonId);
        addKeywordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String keyword = writtenKeyword.getText().toString();
                    JSONPersistentManager.getInstance().addKeywordForAPI(APINames.valueOf(selectedNetworkString.toUpperCase()),keyword);
                    socialMedia.subscribeToKeyword(keyword);
                    currentSubscribedKeywords.setText(listToString(socialMedia.getAllSubscribedKeywords()));
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
                currentSubscribedKeywords.setText(listToString(socialMedia.getAllSubscribedKeywords()));

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