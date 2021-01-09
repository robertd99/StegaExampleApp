package com.example.stegaexampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import de.htw.berlin.steganography.apis.models.APINames;
import de.htw.berlin.steganography.persistence.JSONPersistentManager;

public class AddRemoveKeywordActivity extends AppCompatActivity {

    String selectedNetworkString;
    TextView selectedSocialMedia;
    TextView currentSubscribedKeywords;
    EditText writtenKeyword;
    Button addKeywordBtn;
    Button removeKeywordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_keyword);

        selectedNetworkString = getIntent().getStringExtra("selectedNetwork");
        selectedSocialMedia = (TextView) findViewById(R.id.subscribeSocialMediaTypeId);
        selectedSocialMedia.setText(selectedNetworkString);
        currentSubscribedKeywords = (TextView) findViewById(R.id.showCurrentSubscribedKeywordsId);
        writtenKeyword = (EditText) findViewById(R.id.addKeywordId);

        addKeywordBtn = (Button) findViewById(R.id.addKeywordButtonId);
        addKeywordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = writtenKeyword.getText().toString();
                JSONPersistentManager.getInstance().addKeywordForAPI(APINames.valueOf(selectedNetworkString.toUpperCase()),keyword);
                currentSubscribedKeywords.setText(jsonmanagertostring());

            }
        });

        removeKeywordBtn = (Button) findViewById(R.id.removeKeywordButtonId);
        removeKeywordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = writtenKeyword.getText().toString();
                JSONPersistentManager.getInstance().removeKeywordForAPI(APINames.valueOf(selectedNetworkString.toUpperCase()),keyword);
                currentSubscribedKeywords.setText(jsonmanagertostring());

            }
        });



        currentSubscribedKeywords.setText(jsonmanagertostring());

    }

    private String jsonmanagertostring() {
        try {
            String subscribedKeywords = new String();
            List<String> keywordList = JSONPersistentManager.getInstance().getKeywordListForAPI(APINames.valueOf(selectedNetworkString.toUpperCase()));
            for (String string : keywordList) {
                subscribedKeywords += " " + string;
            }
            return subscribedKeywords;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}