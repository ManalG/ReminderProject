package com.example.reminderproject.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.reminderproject.adapter.NoteAdapter;
import com.example.reminderproject.R;
import com.example.reminderproject.classes.Note;
import com.example.reminderproject.classes.NoteCheckbox;
import com.example.reminderproject.classes.NotePhoto;
import com.example.reminderproject.Constants;
import com.example.reminderproject.enums.NoteType;
import com.example.reminderproject.listener.ItemClickListener;
import com.example.reminderproject.listener.ItemLongClickListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final int ADD_NOTE = 145;
    private static final int EDIT_NOTE = 155;

    private ArrayList<Note> notes;
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_add_action).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, AddNewNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        notes = Note.getDefaultList();
        noteAdapter = new NoteAdapter(notes, new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showDetails(position);
            }
        }, new ItemLongClickListener() {
            @Override
            public void onLongClickItem(int position) {
                deleteItem(position);
            }
        });
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE){
            if (resultCode == RESULT_OK && data != null){
                NoteType noteType = (NoteType) data.getSerializableExtra(Constants.EXTRA_NOTE_TYPE);
                int noteColor = data.getIntExtra(Constants.EXTRA_NOTE_COLOR, R.color.blue);
                String textNote = data.getStringExtra(Constants.EXTRA_TEXT);
                switch (noteType){
                    case NOTE_TYPE:
                        notes.add(new Note(textNote, noteColor));
                        break;
                    case NOTE_WITH_CHECKBOX_TYPE:
                        boolean isChecked = data.getBooleanExtra(Constants.EXTRA_CHECKBOX, false);
                        notes.add(new NoteCheckbox(textNote, noteColor, isChecked));
                        break;
                    case NOTE_WITH_PHOTO_TYPE:
                        Uri uri = data.getParcelableExtra(Constants.EXTRA_PHOTO_URI);
                        notes.add(new NotePhoto(textNote, noteColor, uri));
                        break;
                }
                noteAdapter.notifyItemInserted(notes.size() - 1);
            } else {
                Toast.makeText(this, R.string.error_data, Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == EDIT_NOTE) {
            if (resultCode == RESULT_OK && data != null){
                String note = data.getStringExtra(Constants.EXTRA_EDIT_NOTE);
                notes.get(editPosition).setNote(note);
                noteAdapter.notifyDataSetChanged();
            }
        }
    }

    int editPosition;
    private void showDetails(int position){
        editPosition = position;
        Note note = notes.get(position);
        Intent intent = new Intent(MainActivity.this, NoteDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_TEXT, note.getNote());
        intent.putExtra(Constants.EXTRA_NOTE_COLOR, note.getColor());
        startActivityForResult(intent, EDIT_NOTE);
    }

    private void deleteItem(final int position){
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.delete_confirmation)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notes.remove(position);
                        noteAdapter.notifyDataSetChanged();
                        noteAdapter.notifyItemRemoved(position);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
    }

    public void chackbox_action(View view) {
        if(((CheckBox) view).isChecked()){
            ((View) view.getParent()).setBackgroundColor(getResources().getColor(R.color.green));
        }
    }
}