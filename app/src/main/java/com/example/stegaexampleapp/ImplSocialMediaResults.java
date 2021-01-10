package com.example.stegaexampleapp;

import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.htw.berlin.steganography.apis.SocialMediaListener;

import static java.lang.String.join;

public class ImplSocialMediaResults implements SocialMediaListener {
    List<String> resultList = new ArrayList<>();
    long lastTimeChecked;
    TextView resultsTextView;
    String apiname;

    public ImplSocialMediaResults(TextView textView){
        resultsTextView = textView;
    }


    @Override
    public void updateSocialMediaMessage(List<String> list, String s) {
        for(String string: list){
            resultList.add(string);
        }
        Log.i("write message to log", writeMessageToLog());
    }

    private String writeMessageToLog() {
       return String.join(", ", resultList);
    }

    @Override
    public void updateSocialMediaLastTimeChecked(long l, String s) {
        Log.i("socialMediaListener last time checked call", "social media lsitener last time checked got called with  "+String.valueOf(l));
        lastTimeChecked = l;
        Log.i("final last time checked", String.valueOf(l));

    }


}
