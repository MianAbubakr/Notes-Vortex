package com.pl.notesvortex.Welcome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pl.notesvortex.R;
import com.pl.notesvortex.controller.MainActivity;
import com.pl.notesvortex.databinding.ActivityLoginBinding;

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListener();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListener() {
        binding.rootLogin.setOnTouchListener((v, event) -> {
            hideSoftKeyboard(Login.this, binding.etEmail);
            return false;
        });

        binding.textViewCreateAcc.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, CreateAccount.class);
            startActivity(intent);
            finish();
        });

        binding.loginButton.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString();
            String password = binding.etPassword.getText().toString();

            boolean isValidated = validateData(email, password);
            if (!isValidated) {
                return;
            }

            loginAccountInFirebase(email, password);
        });

        binding.etPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (binding.etPassword.getRight() - binding.etPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // Toggle password visibility
                    int inputType = binding.etPassword.getInputType();
                    if (inputType == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        binding.etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide, 0);
                    } else {
                        binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        binding.etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide, 0);
                    }
                    binding.etPassword.setSelection(binding.etPassword.getText().length());
                    return true;
                }
            }
            return false;
        });
    }

    public void hideSoftKeyboard(Context context, EditText editText) {
        editText.clearFocus();
        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void loginAccountInFirebase(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            changeInProgress(false);
            if (task.isSuccessful()) {
                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(Login.this, "Email not verified, Please verify your email.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Login.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void changeInProgress(boolean inProgress) {
        if (inProgress) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.loginButton.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.loginButton.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Email is invalid");
            return false;
        }
        if (password.length() < 6) {
            binding.etPassword.setError("Password lenght is invalid");
            return false;
        }
        return true;
    }
}