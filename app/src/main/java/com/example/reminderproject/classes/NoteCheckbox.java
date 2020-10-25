package com.example.reminderproject.classes;

public class NoteCheckbox extends Note {

    private boolean isChecked;

    public NoteCheckbox(String note, int color,  boolean isChecked){
        super(note, color);
        this.isChecked = isChecked;
    }

    public boolean getCheckedStatus(){
        return isChecked;
    }

}
