package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private EditText rPass, rEmail;
    private Button rButton;
    private TextView rText;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        rPass = findViewById(R.id.password);
        rEmail = findViewById(R.id.username);
        rButton = findViewById(R.id.bLogin);
        rText = findViewById(R.id.create);
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        rButton.setOnClickListener(v -> check());
        rText.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this,
                    RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }


    private void check() {
        String email = rEmail.getText().toString().trim();
        String pass = rPass.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            Toast.makeText(Login.this,
                    "Fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.setTitle("Login");
        dialog.setMessage("Please wait");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            //todo something
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);

                            finish();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(Login.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    @Override
    protected void onDestroy() {
        dialog.dismiss();
        super.onDestroy();
    }
}