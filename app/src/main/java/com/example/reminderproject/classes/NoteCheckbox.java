package com.example.reminderproject.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class NoteCheckbox extends Note implements Parcelable {

    private boolean isChecked;

    public NoteCheckbox() {

    }

    public NoteCheckbox(String note, int color, boolean isChecked){
        super(note, color);
        this.isChecked = isChecked;
    }

    public boolean getCheckedStatus(){
        return isChecked;
    }

    public void setCheckedStatus(boolean checked) {
        isChecked = checked;
    }

    public NoteCheckbox(Parcel in){
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeBoolean(this.isChecked);
    }

    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        this.isChecked = (in.readInt() == 0) ? false : true;
    }

        public static Parcelable.Creator<NoteCheckbox> CREATOR = new Parcelable.Creator<NoteCheckbox>() {
        @Override
        public NoteCheckbox createFromParcel(Parcel source) {
            return new NoteCheckbox(source);
        }

        @Override
        public NoteCheckbox[] newArray(int size) {
            return new NoteCheckbox[size];
        }
    };

}
