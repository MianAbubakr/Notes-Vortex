package com.pl.notesvortex.Welcome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pl.notesvortex.R;
import com.pl.notesvortex.databinding.ActivityCreateAccountBinding;

public class CreateAccount extends AppCompatActivity {
    ActivityCreateAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListener();
    }

    private void setListener() {
        binding.textViewLogin.setOnClickListener(v -> {
            Intent intent = new Intent(CreateAccount.this, Login.class);
            startActivity(intent);
            finish();
        });

        binding.createAccountButton.setOnClickListener(v -> {
            String email = binding.emailET.getText().toString();
            String password = binding.passwordEt.getText().toString();
            String confirmPassword = binding.confirmPasswordET.getText().toString();

            boolean isValidated = validateData(email, password, confirmPassword);
            if (!isValidated) {
                return;
            }

            createAccountInFirebase(email, password);
        });
    }

    private void createAccountInFirebase(String email, String password) {
        changeInProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccount.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateAccount.this, "Successfully create account, Check email to verify", Toast.LENGTH_SHORT).show();
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        } else {
                            Toast.makeText(CreateAccount.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void changeInProgress(boolean inProgress) {
        if (inProgress) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.createAccountButton.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.createAccountButton.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password, String confirmPassword) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailET.setError("Email is invalid");
            return false;
        }
        if (password.length() < 6) {
            binding.passwordEt.setError("Password lenght is invalid");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            binding.confirmPasswordET.setError("Password not matched");
            return false;
        }
        return true;
    }
}