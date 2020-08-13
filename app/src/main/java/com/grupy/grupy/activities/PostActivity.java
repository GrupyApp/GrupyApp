package com.grupy.grupy.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.grupy.grupy.R;
import com.grupy.grupy.fragments.HomeFragment;
import com.grupy.grupy.models.Post;
import com.grupy.grupy.providers.AuthProvider;
import com.grupy.grupy.providers.ImageProvider;
import com.grupy.grupy.providers.PostProvider;
import com.grupy.grupy.utils.FileUtil;

import java.io.File;

public class PostActivity extends AppCompatActivity {

    ImageView mImageViewPost;
    File mImageFile;
    private final int GALLERY_REQUEST_CODE = 1;
    Button mButtonCreate;
    ImageProvider mImageProvider;
    PostProvider mPostProvider;
    AuthProvider mAuthProvider;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputDescription;
    String mName = "";
    String mDescription = "";
    //string category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mImageViewPost = findViewById(R.id.imageViewPost);
        mButtonCreate = findViewById(R.id.btnCreate);

        mTextInputName = findViewById(R.id.textInputName);
        mTextInputDescription = findViewById(R.id.textInputDescription);

        mImageProvider = new ImageProvider();
        mPostProvider = new PostProvider();
        mAuthProvider = new AuthProvider();

        mButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCreate();
            }
        });

        mImageViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void clickCreate() {

        mName = mTextInputName.getText().toString();
        mDescription = mTextInputDescription.getText().toString();

        if (!mName.isEmpty() && !mDescription.isEmpty()) {
            if (mImageFile != null) {
                saveImage();
            }
        }
        else {
            Toast.makeText(this, "Completed all fields", Toast.LENGTH_LONG).show();
        }

    }

    private void saveImage() {
        mImageProvider.save(PostActivity.this, mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    mImageProvider.getmStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Post post = new Post();
                            post.setImage(url);
                            post.setName(mName);
                            post.setDescription(mDescription);
                            post.setIdUser(mAuthProvider.getUid());

                            mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> taskSave) {
                                    if (taskSave.isSuccessful()) {
                                        Toast.makeText(PostActivity.this, "Group saved", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(PostActivity.this, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(PostActivity.this, "Unable to save the group", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
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