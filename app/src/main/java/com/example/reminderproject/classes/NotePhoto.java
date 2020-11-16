package com.example.reminderproject.classes;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class NotePhoto extends Note {

    public NotePhoto() {

    }

    private Uri image;

    public NotePhoto(String note, int color, Uri image){
        super(note, color);
        this.image = image;
    }

    public Uri getImage(){
        return image;
    }

    public NotePhoto(Parcel in){
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.image, flags);
    }

    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        this.image = in.readParcelable(Uri.class.getClassLoader());
    }

    public static Parcelable.Creator<NotePhoto> CREATOR = new Parcelable.Creator<NotePhoto>() {
        @Override
        public NotePhoto createFromParcel(Parcel source) {
            return new NotePhoto(source);
        }

        @Override
        public NotePhoto[] newArray(int size) {
            return new NotePhoto[size];
        }
    };

}
