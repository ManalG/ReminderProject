package com.example.reminderproject.classes;

import android.graphics.Color;

import com.example.reminderproject.R;

import java.io.Serializable;
import java.util.ArrayList;

public class Note {

    private String note;
    private int color;

    public Note(String note, int color){
        this.note = note;
        this.color = color;
    }

    public String getNote(){
        return note;
    }

    public void setNote(String note){
        this.note = note;
    }

    public int getColor(){
        return color;
    }

    public void setColor(int color){
        this.color = color;
    }

    public static ArrayList<Note> getDefaultList() {
        ArrayList<Note> defaultList = new ArrayList<>();

        defaultList.add(new Note("أهلا بكم في تطبيق المذكرة" , Color.parseColor("#257EA1")));
        defaultList.add(new Note("Welcome to Reminder App." , Color.parseColor("#DCAD40")));

        return  defaultList;
    }

}
