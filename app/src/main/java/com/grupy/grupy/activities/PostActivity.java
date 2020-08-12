package com.grupy.grupy.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;
import com.grupy.grupy.R;
import com.grupy.grupy.providers.ImageProvider;
import com.grupy.grupy.utils.FileUtil;

import java.io.File;

public class PostActivity extends AppCompatActivity {

    ImageView mImageViewPost;
    File mImageFile;
    private final int GALLERY_REQUEST_CODE = 1;
    Button mButtonCreate;
    ImageProvider mImageProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mImageViewPost = findViewById(R.id.imageViewPost);
        mButtonCreate = findViewById(R.id.btnCreate);

        mImageProvider = new ImageProvider();

        mButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });

        mImageViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void saveImage() {
        mImageProvider.save(PostActivity.this, mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PostActivity.this, "Image saved.", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(PostActivity.this, "Image could not be saved.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                mImageFile = FileUtil.from(this, data.getData());
                mImageViewPost.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            } catch (Exception e) {
                Log.d( "Error", "Error: " + e.getMessage());
                Toast.makeText(this, "An error has ocurred:" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}