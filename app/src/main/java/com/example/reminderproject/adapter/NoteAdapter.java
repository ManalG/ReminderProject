package com.example.reminderproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminderproject.R;
import com.example.reminderproject.classes.Note;
import com.example.reminderproject.classes.NoteCheckbox;
import com.example.reminderproject.classes.NotePhoto;
import com.example.reminderproject.listener.ItemClickListener;
import com.example.reminderproject.listener.ItemLongClickListener;

import java.util.ArrayList;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private final static int NOTE_TYPE = 0;
    private final static int CHECKBOX_NOTE_TYPE = 1;
    private final static int PHOTO_NOTE_TYPE = 2;


    ArrayList<Note> notes;
    ItemClickListener itemClickListener;
    ItemLongClickListener itemLongClickListener;

    public NoteAdapter(ArrayList<Note> notes, ItemClickListener itemClickListener, ItemLongClickListener itemLongClickListener) {
        this.notes = notes;
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
    }

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView;
        NoteViewHolder holder;
        if(viewType == CHECKBOX_NOTE_TYPE) {
            rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_note_checkbox, parent, false);
            holder = new CheckableNoteHolder(rootView, itemClickListener, itemLongClickListener);
        } else if (viewType == PHOTO_NOTE_TYPE) {
            rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_note_photo, parent, false);
            holder = new PhotoNoteHolder(rootView, itemClickListener, itemLongClickListener);
        } else {
            rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_note, parent, false);
            holder = new NoteViewHolder(rootView, itemClickListener, itemLongClickListener);
        }

        holder.textView = rootView.findViewById(R.id.card_note_text);

        if (viewType == CHECKBOX_NOTE_TYPE){
            CheckableNoteHolder checkableHolder = (CheckableNoteHolder) holder;
            ((CheckableNoteHolder) holder).checkBox = rootView.findViewById(R.id.card_checkbox_box);
        } else if (viewType == PHOTO_NOTE_TYPE){
            PhotoNoteHolder photoNoteHolder = (PhotoNoteHolder) holder;
            ((PhotoNoteHolder) holder).imageView = rootView.findViewById(R.id.card_image_photo);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.bind(note);
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private int position;

        public void bind(Note note){
            textView.setText(note.getNote());
            itemView.setBackgroundColor(note.getColor());
        }

        public NoteViewHolder(@NonNull View itemView, final ItemClickListener itemClickListener, final ItemLongClickListener itemLongClickListener) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(position);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemLongClickListener.onLongClickItem(position);
                    return true;
                }
            });
        }
    }

    public class CheckableNoteHolder extends NoteViewHolder {

        CheckBox checkBox;

        public CheckableNoteHolder(@NonNull View itemView, ItemClickListener itemClickListener, ItemLongClickListener itemLongClickListener) {
            super(itemView, itemClickListener, itemLongClickListener);
        }

        public void bind(Note note){
            super.bind(note);
            checkBox.setChecked(((NoteCheckbox) note).getCheckedStatus());
        }

    }
    public class PhotoNoteHolder extends NoteViewHolder {

        ImageView imageView;

        public PhotoNoteHolder(@NonNull View itemView, ItemClickListener itemClickListener, ItemLongClickListener itemLongClickListener) {
            super(itemView, itemClickListener, itemLongClickListener);
        }

        public void bind(Note note){
            super.bind(note);
            imageView.setImageURI(((NotePhoto) note).getImage());
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (notes.get(position) instanceof NoteCheckbox) {
            return CHECKBOX_NOTE_TYPE;
        } else if (notes.get(position) instanceof NotePhoto) {
            return PHOTO_NOTE_TYPE;
        } else {
            return NOTE_TYPE;
        }
    }
}

