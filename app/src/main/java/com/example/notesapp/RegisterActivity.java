package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private EditText rPass, rEmail;
    private Button rButton;
    private TextView rText;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        rPass = findViewById(R.id.password);
        rEmail = findViewById(R.id.username);
        rButton = findViewById(R.id.bReg);
        rText = findViewById(R.id.create);
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        rButton.setOnClickListener(v -> check());
        rText.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this,
                    Login.class);
            startActivity(intent);
            finish();
        });


    }


    private void check() {
        String email = rEmail.getText().toString().trim();
        String pass = rPass.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            Toast.makeText(RegisterActivity.this,
                    "Fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.setTitle("Registration");
        dialog.setMessage("Please wait");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(RegisterActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else{
                        dialog.dismiss();
                    Toast.makeText(RegisterActivity.this,
                            "Error " + task.getException(),
                            Toast.LENGTH_SHORT).show();}
                });

    }
}