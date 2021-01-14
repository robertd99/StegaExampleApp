package com.example.stegaexampleapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.htw.berlin.steganography.apis.SocialMedia;
import de.htw.berlin.steganography.apis.SocialMediaListener;
import de.htw.berlin.steganography.apis.models.APINames;
import de.htw.berlin.steganography.persistence.JSONPersistentManager;

import static java.lang.String.join;

public class ImplSocialMediaResults implements SocialMediaListener {
    ManageSocialMediasActivity activity;

    public ImplSocialMediaResults(ManageSocialMediasActivity activity){
         this.activity = activity;
    }


    @Override
    public void updateSocialMediaMessage(SocialMedia socialMedia, List<String> list) {
        Log.i("socialMediaListener updateSocialMediaMessage()", "SocialMedia Type: "+ socialMedia.getApiName() + " Message: "+ writeMessageToLog(list));
        activity.addSearchResultMessages(socialMedia.getApiName(), list);

    }


    @Override
    public void updateSocialMediaLastTimeChecked(SocialMedia socialMedia, String keyword, long l) {
        activity.setLastTimeChecked(socialMedia.getApiName(), keyword, l);
        Log.i("socialMediaListener updateSocialMediaLastTimeChecked()", "SocialMedia Type: "+ socialMedia.getApiName() + " lastTimeChecked: "+String.valueOf(l));
        JSONPersistentManager.getInstance().setLastTimeCheckedForKeywordForAPI(APINames.valueOf(socialMedia.getApiName().toUpperCase()), keyword, l);
    }


    private String writeMessageToLog(List<String> resultList) {
        return String.join(", ", resultList);
    }


}
