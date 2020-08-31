package com.grupy.lineup1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.grupy.lineup1.R;
import com.grupy.lineup1.providers.AuthProvider;

import dmax.dialog.SpotsDialog;

public class LogInActivity extends AppCompatActivity {

    Button mButtonContinue;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    ImageView arrowBack;

    AuthProvider mAuthProvider;
    AlertDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mButtonContinue = findViewById(R.id.btnContinue);
        mTextInputEmail = findViewById(R.id.textInputEmail);
        mTextInputPassword = findViewById(R.id.textInputPassoword);
        arrowBack = findViewById(R.id.arrowBack);

        mAuthProvider = new AuthProvider();

        mButtonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Login in")
                .setCancelable(false).build();
    }

    private void login(){
        String email = mTextInputEmail.getText().toString();
        String password = mTextInputPassword.getText().toString();

        if( email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LogInActivity.this, "Complete login fields", Toast.LENGTH_LONG).show();
        }

        else {
            mDialog.show();
            mAuthProvider.login(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mDialog.dismiss();
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(LogInActivity.this, "Incorrect email or password", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


    }
}