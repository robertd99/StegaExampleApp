package com.example.stegaexampleapp;

import java.util.ArrayList;
import java.util.List;

import de.htw.berlin.steganography.apis.SocialMediaListener;

public class SocialMediaListenerImpl implements SocialMediaListener {

    List<String> messageList = new ArrayList<>();

    @Override
    public void updateSocialMediaMessage(List<String> list) {
        for(String string: list){
            messageList.add(string);
        }
    }

    @Override
    public long updateSocialMediaLastTimeChecked(long l) {
        return 0;
    }
}
