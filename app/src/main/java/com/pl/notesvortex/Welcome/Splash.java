package com.pl.notesvortex.Welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pl.notesvortex.controller.MainActivity;
import com.pl.notesvortex.R;
import com.pl.notesvortex.databinding.ActivitySplashBinding;

public class Splash extends AppCompatActivity {

    ActivitySplashBinding binding;
    Animation topAnim,bottomAnim;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialize();
        setListener();
    }

    private void initialize() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
    }

    private void setListener() {
        binding.imageViewLogo.setAnimation(topAnim);
        binding.textViewLogo.setAnimation(bottomAnim);

        new Handler().postDelayed(() -> {
            if (currentUser == null) {
                startActivity(new Intent(Splash.this, WelcomeScreen.class));
            }else {
                startActivity(new Intent(Splash.this, MainActivity.class));
            }
            finish();
        },3000);
    }
}