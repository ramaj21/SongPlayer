package com.josep.songplayer;

public class Song {
    private String sTitle;
    private int sImage;
    private int sAudio;

    public Song(String title, int image, int audio){
        sTitle = title;
        sImage = image;
        sAudio = audio;
    }

    public String getTitle(){
        return sTitle;
    }

    public int getImage(){
        return sImage;
    }

    public int getAudio(){
        return sAudio;
    }
}
