package com.grupy.grupy.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class PostActivity extends AppCompatActivity {

    ImageView mImageViewPost;
    File mImageFile;
    Button mButtonCreate;
    ImageProvider mImageProvider;
    PostProvider mPostProvider;
    AuthProvider mAuthProvider;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputDescription;
    String mName = "";
    String mDescription = "";
    AlertDialog mDialog;
    //string category;
    AlertDialog.Builder mBuilderSelector;
    CharSequence options[];
    List<Bitmap> bitmaps;

    private final int GALLERY_REQUEST_CODE = 1;
    private final int PHOTO_REQUEST_CODE = 2;
    String mAbsolutePhotoPath;
    String mPhotoPath;
    File mPhotoFile;

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

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Wait")
                .setCancelable(false).build();

        mBuilderSelector = new AlertDialog.Builder(this);
        mBuilderSelector.setTitle("Choose an option");
        options = new CharSequence[] {"Gallery", "Take picture"};

        bitmaps = new ArrayList<>();

        mButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCreate();
            }
        });

        mImageViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage();
            }
        });
    }

    private void selectOptionImage() {

        mBuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0) {
                    openGallery(GALLERY_REQUEST_CODE);
                }
                else if (i == 1) {
                    takePhoto(PHOTO_REQUEST_CODE);
                }
            }
        });

        mBuilderSelector.show();

    }

    private void takePhoto(int requestCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createPhotoFile(requestCode);
            } catch (Exception e) {
                Toast.makeText(this, "An error occurred" + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(PostActivity.this, "com.grupy.grupy", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, PHOTO_REQUEST_CODE);
            }
        }

    }

    private File createPhotoFile(int requestCode) throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(
                new Date() + "_photo",
                ".jpg",
                storageDir
        );  //Aqui sota aniria if per cada request code
        mPhotoPath = "file:" + photoFile.getAbsolutePath();
        mAbsolutePhotoPath = photoFile.getAbsolutePath();
        return photoFile;
    }

    private void clickCreate() {

        mName = mTextInputName.getText().toString();
        mDescription = mTextInputDescription.getText().toString();

        if (!mName.isEmpty() && !mDescription.isEmpty()) {
            if (!bitmaps.isEmpty()) {          //Image from Gallery
                saveGroup(bitmaps);
            }
            /*else if (mPhotoFile != null) {     //Image from Camera
                saveImage(mPhotoFile);
            }*/
        }
        else {
            Toast.makeText(this, "Complete all fields", Toast.LENGTH_LONG).show();
        }

    }

    private void saveGroup(final List<Bitmap> bitmapList) {
        mDialog.show();
        final Post post = new Post();
        for (int i = 0; i < bitmapList.size(); i++) {
            final int j = i;
            mImageProvider.save2(bitmapList.get(i)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        mImageProvider.getmStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                post.addImageUrl(url);
                                if (j == bitmapList.size()-1) {
                                    post.setName(mName);
                                    post.setDescription(mDescription);
                                    post.setIdUser(mAuthProvider.getUid());
                                    mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> taskSave) {
                                            mDialog.dismiss();
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
                            }
                        });
                    }
                    else {
                        mDialog.dismiss();
                        Toast.makeText(PostActivity.this, "Image could not be saved.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    /*
    private void saveImage(File imageFile) {
        mDialog.show();
        mImageProvider.save(PostActivity.this, imageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    mDialog.dismiss();
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
                    mDialog.dismiss();
                    Toast.makeText(PostActivity.this, "Image could not be saved.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }*/

    private void openGallery(int GALLERY_REQUEST_CODE) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Select image from Gallery
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {

            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();

                    try {
                        InputStream is = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        bitmaps.add(bitmap);


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Uri imageUri = data.getData();
                try {
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    bitmaps.add(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            /*
            new Thread(new Runnable() {
                @Override
                public void run() {

                    for (final Bitmap b: bitmaps) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mImageViewPost.setImageBitmap(b);
                            }
                        });
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();*/
            /*try {
                mPhotoFile = null;
                mImageFile = FileUtil.from(this, data.getData());
                mImageViewPost.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            } catch (Exception e) {
                Log.d( "Error", "Error: " + e.getMessage());
                Toast.makeText(this, "An error has ocurred:" + e.getMessage(), Toast.LENGTH_LONG).show();
            }*/
        }

        //Take Picture
        if (requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            mImageFile = null;
            mPhotoFile = new File(mAbsolutePhotoPath);
            Picasso.with(PostActivity.this).load(mPhotoPath).into(mImageViewPost);
        }
    }
}