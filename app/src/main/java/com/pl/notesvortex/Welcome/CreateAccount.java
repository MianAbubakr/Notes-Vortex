package com.pl.notesvortex.Welcome;

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

    @SuppressLint("ClickableViewAccessibility")
    private void setListener() {
        binding.rootCreateAccount.setOnTouchListener((v, event) -> {
            hideSoftKeyboard(CreateAccount.this, binding.emailET);
            return false;
        });

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

        binding.passwordEt.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (binding.passwordEt.getRight() - binding.passwordEt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // Toggle password visibility
                    int inputType = binding.passwordEt.getInputType();
                    if (inputType == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        binding.passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        binding.passwordEt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide, 0);
                    } else {
                        binding.passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        binding.passwordEt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide, 0);
                    }
                    binding.passwordEt.setSelection(binding.passwordEt.getText().length()); // Maintain cursor position
                    return true;
                }
            }
            return false;
        });

        binding.confirmPasswordET.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (binding.confirmPasswordET.getRight() - binding.confirmPasswordET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // Toggle password visibility
                    int inputType = binding.confirmPasswordET.getInputType();
                    if (inputType == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        binding.confirmPasswordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        binding.confirmPasswordET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide, 0);
                    } else {
                        binding.confirmPasswordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        binding.confirmPasswordET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide, 0);
                    }
                    binding.confirmPasswordET.setSelection(binding.confirmPasswordET.getText().length());
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

    private void createAccountInFirebase(String email, String password) {
        changeInProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccount.this, task -> {
            changeInProgress(false);
            if (task.isSuccessful()) {
                Toast.makeText(CreateAccount.this, "Successfully create account, Check email to verify", Toast.LENGTH_SHORT).show();
                firebaseAuth.getCurrentUser().sendEmailVerification();
                firebaseAuth.signOut();
                finish();
            } else {
                Toast.makeText(CreateAccount.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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