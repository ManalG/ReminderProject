package com.example.reminderproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.reminderproject.R;
import com.example.reminderproject.Constants;

public class NoteDetailsActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        editText = findViewById(R.id.edit_note_text);
        editText.setText(getIntent().getStringExtra(Constants.EXTRA_TEXT));

        int color = getIntent().getIntExtra(Constants.EXTRA_NOTE_COLOR, getResources().getColor(R.color.blue));
        findViewById(R.id.note_data_container).setBackgroundColor(color);

    }

    public void edit_note_action(View view){
        if (getNoteText().isEmpty() != true) {
            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA_EDIT_NOTE, getNoteText());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(NoteDetailsActivity.this, R.string.set_text_massage, Toast.LENGTH_LONG).show();
        }
    }

    private String getNoteText(){
        return editText.getText().toString();
    }


}