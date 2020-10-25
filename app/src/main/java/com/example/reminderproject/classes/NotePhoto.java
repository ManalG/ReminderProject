package com.example.reminderproject.classes;

import android.net.Uri;

public class NotePhoto extends Note {

    private Uri image;

    public NotePhoto(String note, int color, Uri image){
        super(note, color);
        this.image = image;
    }

    public Uri getImage(){
        return image;
    }

}
