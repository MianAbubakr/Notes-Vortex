package com.pl.notesvortex.Welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.pl.notesvortex.R;
import com.pl.notesvortex.databinding.ActivityWelcomeScreenBinding;

public class WelcomeScreen extends AppCompatActivity {
    ActivityWelcomeScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
    }

    private void setListener() {
        binding.include.getStartedButton.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeScreen.this, Login.class);
            startActivity(intent);
            finish();
        });
    }
}