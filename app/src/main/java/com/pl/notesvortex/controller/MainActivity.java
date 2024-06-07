package com.pl.notesvortex.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.pl.notesvortex.R;
import com.pl.notesvortex.Utility;
import com.pl.notesvortex.Welcome.Login;
import com.pl.notesvortex.addNotes.NotesDetails;
import com.pl.notesvortex.addNotes.model.NoteModel;
import com.pl.notesvortex.controller.adapter.NotesAdapter;
import com.pl.notesvortex.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
        setupRecyclerView();
    }

    private void setListener() {
        binding.logoutButton.setOnClickListener(v -> {
            logout();
        });

        binding.addNotesButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, NotesDetails.class));
        });
    }

    private void logout() {
        Dialog dialog = new Dialog(MainActivity.this, R.style.CustomDialog);
        dialog.setContentView(R.layout.include_logout_dialog);

        Button yes = dialog.findViewById(R.id.yesButton);
        Button no = dialog.findViewById(R.id.noButton);

        yes.setOnClickListener(v12 -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        });

        no.setOnClickListener(v1 -> dialog.dismiss());
        dialog.show();
    }

    private void setupRecyclerView() {
        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<NoteModel> options = new FirestoreRecyclerOptions.Builder<NoteModel>()
                .setQuery(query, NoteModel.class).build();
        binding.notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NotesAdapter(options, this);
        binding.notesRecyclerView.setAdapter(notesAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        notesAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        notesAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        notesAdapter.notifyDataSetChanged();
    }
}