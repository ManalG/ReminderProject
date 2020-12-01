package com.example.reminderproject.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.reminderproject.R;
import com.example.reminderproject.Constants;
import com.example.reminderproject.classes.Note;
import com.example.reminderproject.classes.NoteCheckbox;
import com.example.reminderproject.classes.NotePhoto;
import com.example.reminderproject.enums.NoteType;

public class NoteDetailsActivity extends AppCompatActivity {

    private static final int EDIT_IMAGE = 121;

    private ImageView imageViewNote;
    private EditText editText;
    private CheckBox checkBox;
    private View noteView;

    private NoteType currentType;
    private Uri selectedImageUri;
    private Note editingNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        noteView = findViewById(R.id.note_data_container);
        imageViewNote = findViewById(R.id.edit_image_note);
        editText = findViewById(R.id.edit_note_text);
        checkBox = findViewById(R.id.edit_check_box);

        editingNote = getIntent().getParcelableExtra(Constants.EXTRA_NOTE_OBJECT);
        String textNote = editingNote.getNote();
        int noteColor = editingNote.getColor();
        currentType = NoteType.NOTE_TYPE;

        editText.setText(textNote);
        noteView.setBackgroundColor(noteColor);

        if (editingNote.getClass() == NoteCheckbox.class){
            boolean checkboxStatus = ((NoteCheckbox) editingNote).getCheckedStatus();
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(checkboxStatus);
            currentType = NoteType.NOTE_WITH_CHECKBOX_TYPE;
        } else if (editingNote.getClass() == NotePhoto.class){
            selectedImageUri = ((NotePhoto) editingNote).getImage();
            imageViewNote.setVisibility(View.VISIBLE);
            imageViewNote.setImageURI(selectedImageUri);
            currentType = NoteType.NOTE_WITH_PHOTO_TYPE;
        }

    }

    public void edit_note_action(View view){
        if (getNoteText().isEmpty() != true) {
            Intent intent = new Intent();
            editingNote.setNote(getNoteText());
            if (currentType == NoteType.NOTE_WITH_CHECKBOX_TYPE) {
                ((NoteCheckbox) editingNote ).setCheckedStatus(checkBox.isChecked());
            } else if (currentType == NoteType.NOTE_WITH_PHOTO_TYPE && getNoteImage() != null) {
                ((NotePhoto) editingNote).setImage(getNoteImage());
            }
            intent.putExtra(Constants.EXTRA_EDIT_NOTE, editingNote);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, R.string.set_text_massage, Toast.LENGTH_LONG).show();
        }
    }

    private String getNoteText(){
        return editText.getText().toString();
    }

    public void image_action(View view){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), EDIT_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EDIT_IMAGE){
            if(resultCode == RESULT_OK && data != null && data.getData() != null){
                setSelectedPhoto(data.getData());
                getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                Toast.makeText(this, R.string.faild_to_get_image, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setSelectedPhoto(Uri uri){
        imageViewNote.setImageURI(uri);
        selectedImageUri = uri;
    }

    private Uri getNoteImage(){
        if (selectedImageUri != null){
            return selectedImageUri;
        } else {
            Toast.makeText(this, R.string.set_image_massage, Toast.LENGTH_LONG).show();
            return null;
        }
    }

}