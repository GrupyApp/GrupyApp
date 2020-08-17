package com.grupy.grupy.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseExceptionMapper;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.grupy.grupy.models.Post;

public class PostProvider {

    CollectionReference mCollection;

    public PostProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("Groups");
    }

    public Task<Void> save(Post post) {
        return mCollection.document().set(post);
    }
}