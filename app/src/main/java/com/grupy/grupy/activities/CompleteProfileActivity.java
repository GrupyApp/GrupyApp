package com.grupy.grupy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.grupy.grupy.R;
import com.grupy.grupy.models.User;
import com.grupy.grupy.providers.AuthProvider;
import com.grupy.grupy.providers.UserProvider;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class CompleteProfileActivity extends AppCompatActivity {

    TextInputEditText mTextInputUsername;
    Button mButtonFinish;
    AuthProvider mAuthProvider;
    UserProvider mUserProvider;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        mTextInputUsername = findViewById(R.id.textInputUsername);
        mButtonFinish = findViewById(R.id.btnFinish);

        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();
        mButtonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Creating account")
                .setCancelable(false).build();

    }

    private void register(){
        String username = mTextInputUsername.getText().toString();

        if(!username.isEmpty() ) {
            updateUser(username);
        }
        else{
            Toast.makeText(this, "A username is needed", Toast.LENGTH_LONG).show();
        }
    }

    private void updateUser(final String username) {
        String id = mAuthProvider.getUid();
        User user = new User();
        user.setUsername(username);
        user.setId(id);
        mDialog.show();
        mUserProvider.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()) {
                    Intent intent = new Intent(CompleteProfileActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(CompleteProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}