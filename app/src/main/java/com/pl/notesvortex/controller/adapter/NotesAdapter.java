package com.pl.notesvortex.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.pl.notesvortex.R;
import com.pl.notesvortex.addNotes.model.NoteModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotesAdapter extends FirestoreRecyclerAdapter<NoteModel, NotesAdapter.NoteViewHolder> {
    Context context;
    public NotesAdapter(@NonNull FirestoreRecyclerOptions<NoteModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull NoteModel noteModel) {
        holder.titleTextView.setText(noteModel.getTitle());
        holder.contentTextView.setText(noteModel.getContent());

        Timestamp timestamp = noteModel.getTimestamp();
        Date date = timestamp.toDate();
        SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        String formattedDate = outputFormat.format(date);
        holder.timeStampTextView.setText(formattedDate);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView, contentTextView, timeStampTextView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleText);
            contentTextView = itemView.findViewById(R.id.contentText);
            timeStampTextView = itemView.findViewById(R.id.timeStampText);
        }
    }
}
