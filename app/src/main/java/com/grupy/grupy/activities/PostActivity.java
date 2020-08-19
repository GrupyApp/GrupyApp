package com.grupy.grupy.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class PostActivity extends AppCompatActivity {

    ImageView mImageViewPost1;
    ImageView mImageViewPost2;
    ImageView mImageViewPost3;
    ImageView mImageViewPost4;
    ImageView mImageViewPost5;
    ImageView mImageViewPost6;

    /*
    File mImageFile1;
    File mImageFile2;
    File mImageFile3;
    File mImageFile4;
    File mImageFile5;
    File mImageFile6;

    File mPhotoFile1;
    File mPhotoFile2;
    File mPhotoFile3;
    File mPhotoFile4;
    File mPhotoFile5;
    File mPhotoFile6;
    */

    List<File> mPhotoList;
    List<File> mImageList;
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

    private final int GALLERY_REQUEST_CODE1 = 1;
    private final int GALLERY_REQUEST_CODE2 = 2;
    private final int GALLERY_REQUEST_CODE3 = 3;
    private final int GALLERY_REQUEST_CODE4 = 4;
    private final int GALLERY_REQUEST_CODE5 = 5;
    private final int GALLERY_REQUEST_CODE6 = 6;
    private final int PHOTO_REQUEST_CODE1 = 7;
    private final int PHOTO_REQUEST_CODE2 = 8;
    private final int PHOTO_REQUEST_CODE3 = 9;
    private final int PHOTO_REQUEST_CODE4 = 10;
    private final int PHOTO_REQUEST_CODE5 = 11;
    private final int PHOTO_REQUEST_CODE6 = 12;
    String mAbsolutePhotoPath1;
    String mAbsolutePhotoPath2;
    String mAbsolutePhotoPath3;
    String mAbsolutePhotoPath4;
    String mAbsolutePhotoPath5;
    String mAbsolutePhotoPath6;
    String mPhotoPath1;
    String mPhotoPath2;
    String mPhotoPath3;
    String mPhotoPath4;
    String mPhotoPath5;
    String mPhotoPath6;

    String mUrl = "";

    //vector de files


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mImageViewPost1 = findViewById(R.id.imageViewPost1);
        mImageViewPost2 = findViewById(R.id.imageViewPost2);
        mImageViewPost3 = findViewById(R.id.imageViewPost3);
        mImageViewPost4 = findViewById(R.id.imageViewPost4);
        mImageViewPost5 = findViewById(R.id.imageViewPost5);
        mImageViewPost6 = findViewById(R.id.imageViewPost6);

        mImageList = new ArrayList<File>();
        mImageList.add(null);
        mImageList.add(null);
        mImageList.add(null);
        mImageList.add(null);
        mImageList.add(null);
        mImageList.add(null);

        mPhotoList = new ArrayList<File>();
        mPhotoList.add(null);
        mPhotoList.add(null);
        mPhotoList.add(null);
        mPhotoList.add(null);
        mPhotoList.add(null);
        mPhotoList.add(null);


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

        mButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCreate();
            }
        });


        //OnClickListener for all different ViewPosts

        mImageViewPost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(1);
            }
        });

        mImageViewPost2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(2);
            }
        });

        mImageViewPost3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(3);
            }
        });

        mImageViewPost4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(4);
            }
        });

        mImageViewPost5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(5);
            }
        });

        mImageViewPost6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(6);
            }
        });
    }


    private void selectOptionImage(final int numberImage) {

        mBuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //si i == 0 volem la galeria
                if (i == 0) {
                    if (numberImage == 1) {
                        openGallery(GALLERY_REQUEST_CODE1);
                    }
                    else if (numberImage == 2) {
                        openGallery(GALLERY_REQUEST_CODE2);
                    }
                    else if (numberImage == 3) {
                        openGallery(GALLERY_REQUEST_CODE3);

                    }
                    else if (numberImage == 4) {
                        openGallery(GALLERY_REQUEST_CODE4);

                    }
                    else if (numberImage == 5) {
                        openGallery(GALLERY_REQUEST_CODE5);

                    }
                    else if (numberImage == 6) {
                        openGallery(GALLERY_REQUEST_CODE6);

                    }

                }

                //si i == 1 vole fer una foto
                else if (i == 1) {
                    if (numberImage == 1) {
                        takePhoto(PHOTO_REQUEST_CODE1);
                    }
                    else if (numberImage == 2) {
                        takePhoto(PHOTO_REQUEST_CODE2);
                    }
                    else if (numberImage == 3) {
                        takePhoto(PHOTO_REQUEST_CODE3);
                    }
                    else if (numberImage == 4) {
                        takePhoto(PHOTO_REQUEST_CODE4);
                    }
                    else if (numberImage == 5) {
                        takePhoto(PHOTO_REQUEST_CODE5);
                    }
                    else if (numberImage == 6) {
                        takePhoto(PHOTO_REQUEST_CODE6);
                    }
                }
            }
        });

        mBuilderSelector.show();

    }


    /////////////////////////////////////////////////////////////
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
                startActivityForResult(takePictureIntent, requestCode);
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

        if (requestCode == PHOTO_REQUEST_CODE1) {
            mPhotoPath1 = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath1 = photoFile.getAbsolutePath();
        }
        else if (requestCode == PHOTO_REQUEST_CODE2) {
            mPhotoPath2 = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath2 = photoFile.getAbsolutePath();
        }
        else if (requestCode == PHOTO_REQUEST_CODE2) {
            mPhotoPath3 = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath3 = photoFile.getAbsolutePath();
        }
        else if (requestCode == PHOTO_REQUEST_CODE2) {
            mPhotoPath4 = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath4 = photoFile.getAbsolutePath();
        }
        else if (requestCode == PHOTO_REQUEST_CODE2) {
            mPhotoPath5 = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath5 = photoFile.getAbsolutePath();
        }
        else if (requestCode == PHOTO_REQUEST_CODE2) {
            mPhotoPath6 = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath6 = photoFile.getAbsolutePath();
        }


        return photoFile;
    }

    ///////////////////////////////////////////////////////////////////

    //fes totes les diferents permutacions 7:08
    private void clickCreate() {

        mName = mTextInputName.getText().toString();
        mDescription = mTextInputDescription.getText().toString();

        if (!mName.isEmpty() && !mDescription.isEmpty()) {
            saveGroup();
        }
        else {
            Toast.makeText(this, "Complete all fields", Toast.LENGTH_LONG).show();
        }

    }

    //do
    private void saveGroup() {
        mDialog.show();
        Post post = new Post();
        for (int i = 0; i < mImageList.size(); i++) {
            if (mImageList.get(i) != null) {
                saveImage(mImageList.get(i));
                post.setImage1(mUrl);
                Toast.makeText(PostActivity.this, mUrl, Toast.LENGTH_LONG).show();
            }
            else if(mPhotoList.get(i) != null) {
                saveImage(mPhotoList.get(i));
                post.addPhoto(mUrl);
            }
        }

        post.setName(mName);
        post.setDescription(mDescription);
        post.setIdUser(mAuthProvider.getUid());

        mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> taskSave) {
                mDialog.dismiss();
                if (taskSave.isSuccessful()) {
                    //Toast.makeText(PostActivity.this, "Group saved", Toast.LENGTH_LONG).show();
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

    private void saveImage(final File image) {
        mImageProvider.save(PostActivity.this, image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    mImageProvider.getmStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            mUrl = uri.toString();
                        }
                    });
                } else {
                    mDialog.dismiss();
                    Toast.makeText(PostActivity.this, "Image could not be saved.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

/*
    private void saveImage2(final File mImageFile1, final File mImageFile2) {
        mDialog.show();
        //with image provider in orther to save it
        mImageProvider.save(PostActivity.this, mImageFile1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    mImageProvider.getmStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri1) {
                            final String url1 = uri1.toString();
                            //iterate the same process for other images
                            mImageProvider.save(PostActivity.this,mImageFile2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImage2) {
                                    if (taskImage2.isSuccessful()) {
                                        mImageProvider.getmStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri2) {
                                                String url2 = uri2.toString();

                                                //save the post using post provider
                                                Post post = new Post();
                                                post.setImage1(url1);
                                                post.setImage2(url2);
                                            }
                                        });
                                    } else {
                                        mDialog.dismiss();
                                        Toast.makeText(PostActivity.this, "Image2 could not be saved.", Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                        }
                    });
                }
                else {
                    mDialog.dismiss();
                    Toast.makeText(PostActivity.this, "Image1 could not be saved.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

 */

    private void openGallery(int request_code) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, request_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Select image from Gallery
        if (requestCode == GALLERY_REQUEST_CODE1 && resultCode == RESULT_OK) {
            try {
                mPhotoList.set(0,null);
                mImageList.set(0,FileUtil.from(this, data.getData()));
                mImageViewPost1.setImageBitmap(BitmapFactory.decodeFile(mImageList.get(0).getAbsolutePath()));
            } catch (Exception e) {
                Log.d( "Error", "Error: " + e.getMessage());
                Toast.makeText(this, "An error has ocurred:" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == GALLERY_REQUEST_CODE2 && resultCode == RESULT_OK) {
            try {
                mPhotoList.set(1,null);
                mImageList.set(1,FileUtil.from(this, data.getData()));
                mImageViewPost2.setImageBitmap(BitmapFactory.decodeFile(mImageList.get(1).getAbsolutePath()));
            } catch (Exception e) {
                Log.d( "Error", "Error: " + e.getMessage());
                Toast.makeText(this, "An error has ocurred:" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE3 && resultCode == RESULT_OK) {
            try {
                mPhotoList.set(2,null);
                mImageList.set(2,FileUtil.from(this, data.getData()));
                mImageViewPost3.setImageBitmap(BitmapFactory.decodeFile(mImageList.get(2).getAbsolutePath()));
            } catch (Exception e) {
                Log.d( "Error", "Error: " + e.getMessage());
                Toast.makeText(this, "An error has ocurred:" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE4 && resultCode == RESULT_OK) {
            try {
                mPhotoList.set(3,null);
                mImageList.set(3,FileUtil.from(this, data.getData()));
                mImageViewPost4.setImageBitmap(BitmapFactory.decodeFile(mImageList.get(3).getAbsolutePath()));
            } catch (Exception e) {
                Log.d( "Error", "Error: " + e.getMessage());
                Toast.makeText(this, "An error has ocurred:" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE5 && resultCode == RESULT_OK) {
            try {
                mPhotoList.set(4,null);
                mImageList.set(4,FileUtil.from(this, data.getData()));
                mImageViewPost5.setImageBitmap(BitmapFactory.decodeFile(mImageList.get(4).getAbsolutePath()));
            } catch (Exception e) {
                Log.d( "Error", "Error: " + e.getMessage());
                Toast.makeText(this, "An error has ocurred:" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE6 && resultCode == RESULT_OK) {
            try {
                mPhotoList.set(5,null);
                mImageList.set(5,FileUtil.from(this, data.getData()));
                mImageViewPost6.setImageBitmap(BitmapFactory.decodeFile(mImageList.get(5).getAbsolutePath()));
            } catch (Exception e) {
                Log.d( "Error", "Error: " + e.getMessage());
                Toast.makeText(this, "An error has ocurred:" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        //Take Picture OJO S'HA AL MATEIX METODE PER TAL DE CRIDAR EL ONACTIVITY 3:46
        if (requestCode == PHOTO_REQUEST_CODE1 && resultCode == RESULT_OK) {
            mImageList.set(0,null);
            mPhotoList.set(0,new File(mAbsolutePhotoPath1));
            Picasso.with(PostActivity.this).load(mPhotoPath1).into(mImageViewPost1);
        }
        if (requestCode == PHOTO_REQUEST_CODE2 && resultCode == RESULT_OK) {
            mImageList.set(1,null);
            mPhotoList.set(1,new File(mAbsolutePhotoPath2));
            Picasso.with(PostActivity.this).load(mPhotoPath2).into(mImageViewPost2);
        }
        if (requestCode == PHOTO_REQUEST_CODE3 && resultCode == RESULT_OK) {
            mImageList.set(2,null);
            mPhotoList.set(2,new File(mAbsolutePhotoPath3));
            Picasso.with(PostActivity.this).load(mPhotoPath3).into(mImageViewPost3);
        }
        if (requestCode == PHOTO_REQUEST_CODE4 && resultCode == RESULT_OK) {
            mImageList.set(3,null);
            mPhotoList.set(3,new File(mAbsolutePhotoPath4));
            Picasso.with(PostActivity.this).load(mPhotoPath4).into(mImageViewPost4);
        }
        if (requestCode == PHOTO_REQUEST_CODE5 && resultCode == RESULT_OK) {
            mImageList.set(4,null);
            mPhotoList.set(4,new File(mAbsolutePhotoPath5));
            Picasso.with(PostActivity.this).load(mPhotoPath5).into(mImageViewPost5);
        }
        if (requestCode == PHOTO_REQUEST_CODE6 && resultCode == RESULT_OK) {
            mImageList.set(5,null);
            mPhotoList.set(5,new File(mAbsolutePhotoPath6));
            Picasso.with(PostActivity.this).load(mPhotoPath6).into(mImageViewPost6);
        }
    }
}