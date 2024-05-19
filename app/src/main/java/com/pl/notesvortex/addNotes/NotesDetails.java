package com.pl.notesvortex.addNotes;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pl.notesvortex.R;
import com.pl.notesvortex.Utility;
import com.pl.notesvortex.addNotes.model.NoteModel;
import com.pl.notesvortex.databinding.ActivityNotesDetailsBinding;

public class NotesDetails extends AppCompatActivity {
    ActivityNotesDetailsBinding binding;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNotesDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setListener();
    }

    private void setListener() {
        binding.saveNoteIcon.setOnClickListener(v -> {
            saveNote();
        });
    }

    private void saveNote() {
        String noteTitle = binding.titleET.getText().toString();
        String noteContent = binding.contentET.getText().toString();
        if (noteTitle == null || noteTitle.isEmpty()){
            binding.titleET.setError("Title is required");
            return;
        }

        NoteModel noteModel = new NoteModel();
        noteModel.setTitle(noteTitle);
        noteModel.setContent(noteContent);
        noteModel.setTimestamp(Timestamp.now());
        saveNoteToFirebase(noteModel);
    }

    void saveNoteToFirebase(NoteModel noteModel){
        changeInProgress(true);
        documentReference = Utility.getCollectionReferenceForNotes().document();
        documentReference.set(noteModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                changeInProgress(false);
                if (task.isSuccessful()){
                    Toast.makeText(NotesDetails.this, "Note added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(NotesDetails.this, "Failed while adding note", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void changeInProgress(boolean inProgress) {
        if (inProgress) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.saveNoteIcon.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.saveNoteIcon.setVisibility(View.VISIBLE);
        }
    }
}