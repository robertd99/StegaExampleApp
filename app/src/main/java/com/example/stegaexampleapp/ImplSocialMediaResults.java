package com.example.stegaexampleapp;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.htw.berlin.steganography.apis.SocialMediaListener;

import static java.lang.String.join;

public class ImplSocialMediaResults implements SocialMediaListener {
    List<String> resultList = new ArrayList<>();
    long lastTimeChecked;
    ManageSocialMediasActivity activity;
    String apiname;

    public ImplSocialMediaResults(ManageSocialMediasActivity activity){
         this.activity = activity;
    }


    @Override
    public void updateSocialMediaMessage(List<String> list, String s) {
        for(String string: list){
            resultList.add(string);
        }
        Log.i("socialMediaListener updateSocialMediaMessage()", "SocialMedia Type: "+ s + " Message: "+ writeMessageToLog());
        activity.setSearchResultMessage(resultList, s);
    }

    private String writeMessageToLog() {
       return String.join(", ", resultList);
    }

    @Override
    public void updateSocialMediaLastTimeChecked(long l, String s) {
        lastTimeChecked = l;
        Log.i("socialMediaListener updateSocialMediaLastTimeChecked()", "SocialMedia Type: "+ s + " lastTimeChecked: "+String.valueOf(l));

    }


}
