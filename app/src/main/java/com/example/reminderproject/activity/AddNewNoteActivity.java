package com.example.reminderproject.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.reminderproject.R;
import com.example.reminderproject.Constants;
import com.example.reminderproject.enums.NoteType;

public class AddNewNoteActivity extends AppCompatActivity {

    private static final int READ_PHOTO_FROM_GALLARY_PERMISSION = 130;
    private static final int PICK_IMAGE = 120;

    private EditText editTextNote;
    private CheckBox checkBoxNote;
    private ImageView imageViewNote;
    private NoteType currentType;
    private int currentColor;
    private Uri selectedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);

        editTextNote = findViewById(R.id.edit_text_note);
        checkBoxNote = findViewById(R.id.edit_check_box);
        imageViewNote = findViewById(R.id.edit_image_note);

        RadioGroup checkBoxType = findViewById(R.id.radio_group_type);
        checkBoxType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButtonChecked = group.findViewById(checkedId);
                int current_radio = radioButtonChecked.getId();
                if (radioButtonChecked.isChecked()) {
                    switch (current_radio){
                        case R.id.radio_button_note:
                            prepareNoteType(NoteType.NOTE_TYPE, View.GONE, View.GONE);
                            break;
                        case R.id.radio_button_note_checkbox:
                            prepareNoteType(NoteType.NOTE_WITH_CHECKBOX_TYPE, View.GONE, View.VISIBLE);
                            break;
                        case R.id.radio_button_note_photo:
                            prepareNoteType(NoteType.NOTE_WITH_PHOTO_TYPE, View.VISIBLE, View.GONE);
                            break;
                    }
                }
            }
        });
        RadioGroup checkBoxColor = findViewById(R.id.radio_group_color);
        checkBoxColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButtonChecked = group.findViewById(checkedId);
                int current_radio = radioButtonChecked.getId();
                if (radioButtonChecked.isChecked()) {
                    switch (current_radio){
                        case R.id.radio_button_red:
                            prepareNoteColor(getResources().getColor(R.color.red));
                            break;
                        case R.id.radio_button_yellow:
                            prepareNoteColor(getResources().getColor(R.color.yellow));
                            break;
                        case R.id.radio_button_blue:
                            prepareNoteColor(getResources().getColor(R.color.blue));
                            break;
                    }
                }
            }
        });

        // to set default type and color:
        checkBoxType.check(R.id.radio_button_note);
        checkBoxColor.check(R.id.radio_button_blue);
    }


    private void prepareNoteType(NoteType type, int imageVisibility, int checkboxVisibility){
        currentType = type;
        imageViewNote.setVisibility(imageVisibility);
        checkBoxNote.setVisibility(checkboxVisibility);
    }

    private void prepareNoteColor(int color){
        currentColor = color;
        findViewById(R.id.note_data_container).setBackgroundColor(color);
    }

    public void image_action(View view){
        checkPhotoPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_PHOTO_FROM_GALLARY_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                firePickPhotoIntent();
            } else {
                Toast.makeText(this, R.string.read_permission_needed_to_access_files, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE){
            if(resultCode == RESULT_OK && data != null && data.getData() != null){
                setSelectedPhoto(data.getData());
                getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                Toast.makeText(this, R.string.faild_to_get_image, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkPhotoPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PHOTO_FROM_GALLARY_PERMISSION);
        } else {
            firePickPhotoIntent();
        }
    }

    private void firePickPhotoIntent(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE);
    }

    private void setSelectedPhoto(Uri uri){
        imageViewNote.setImageURI(uri);
        selectedImageUri = uri;
    }


    public void add_note_action(View view){
        if (getNoteText().isEmpty() != true) {
            Intent intent = new Intent();
            switch (currentType){
                case NOTE_WITH_CHECKBOX_TYPE:
                    intent.putExtra(Constants.EXTRA_CHECKBOX, checkBoxNote.isChecked());
                    break;
                case NOTE_WITH_PHOTO_TYPE:
                    if (getNoteImage() != null) {
                        intent.putExtra(Constants.EXTRA_PHOTO_URI, selectedImageUri);
                        break;
                    }
                    return;
            }
            intent.putExtra(Constants.EXTRA_TEXT , getNoteText());
            intent.putExtra(Constants.EXTRA_NOTE_TYPE, currentType);
            intent.putExtra(Constants.EXTRA_NOTE_COLOR, currentColor);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(AddNewNoteActivity.this, R.string.set_text_massage, Toast.LENGTH_LONG).show();
        }
    }

    private String getNoteText(){
        return editTextNote.getText().toString();
    }

    private Uri getNoteImage(){
        if (selectedImageUri != null){
            return selectedImageUri;
        } else {
            Toast.makeText(AddNewNoteActivity.this, R.string.set_image_massage, Toast.LENGTH_LONG).show();
            return null;
        }
    }

}
