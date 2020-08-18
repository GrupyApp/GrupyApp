package com.grupy.grupy.providers;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.grupy.grupy.utils.CompressorBitmapImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.util.Random;

public class ImageProvider {

    StorageReference mStorage;
    Random rand;

    public ImageProvider() {
        mStorage = FirebaseStorage.getInstance().getReference();
        rand = new Random();
    }

    public UploadTask save(Context context, File file) {
        byte[] imageByte = CompressorBitmapImage.getImage(context, file.getPath(), 500, 500);
        StorageReference storage = mStorage.child(new Date() + ".jpg");
        mStorage = storage;
        UploadTask task = storage.putBytes(imageByte);
        return task;
    }

    public UploadTask save2(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 80, stream);
        byte[] byteArray = stream.toByteArray();
        bmp.recycle();
        StorageReference storage = mStorage.child(new Date() + String.valueOf(rand.nextInt(1000)) + ".jpg");
        mStorage = storage;
        UploadTask task = storage.putBytes(byteArray);
        return task;
    }

    public StorageReference getmStorage() {
        return mStorage;
    }
}
