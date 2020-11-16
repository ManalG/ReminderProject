package com.example.reminderproject.classes;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Note implements Parcelable {

    private String note;
    private int color;

    public Note() { }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.note);
        dest.writeInt(this.color);
    }

    public void readFromParcel(Parcel in) {
        this.note = in.readString();
        this.color = in.readInt();
    }

    public Note(Parcel in) {
        readFromParcel(in);
    }

    public static Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

}
