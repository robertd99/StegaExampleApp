package com.example.stegaexampleapp;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.htw.berlin.steganography.apis.SocialMediaListener;

public class ImplSocialMediaResults implements SocialMediaListener {
    List<String> resultList = new ArrayList<>();
    TextView resultsTextView;

    public ImplSocialMediaResults(TextView textView){
        resultsTextView = textView;
    }

    @Override
    public void updateSocialMediaMessage(List<String> list) {
        for(String string: list){
            resultList.add(string);
        }
        String result = new String();
        for(String string: resultList){
            result += " "+string;
        }
        resultsTextView.setText(result);
    }

}
