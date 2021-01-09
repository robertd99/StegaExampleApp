package com.example.stegaexampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SubscribeToActivity extends AppCompatActivity {

    Button redditBtn;
    Button imgurBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_to);

        redditBtn = (Button) findViewById(R.id.subscribeToRedditId);
        redditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddRemoveKeyworActivity("reddit");
            }
        });

        imgurBtn =(Button) findViewById(R.id.subscribeToImgurId);
        imgurBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddRemoveKeyworActivity("imgur");
            }
        });
    }

    private void openAddRemoveKeyworActivity(String network) {
        Intent intent = new Intent(this, AddRemoveKeywordActivity.class);
        intent.putExtra("selectedNetwork", network);
        startActivity(intent);
    }
}