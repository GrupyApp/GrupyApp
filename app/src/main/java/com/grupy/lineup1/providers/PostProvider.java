package com.grupy.lineup1.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.grupy.lineup1.models.Post;

public class PostProvider {

    CollectionReference mCollection;

    public PostProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("Groups");
    }

    public Task<Void> save(Post post) {
        return mCollection.document().set(post);
    }

    public Query getAll() {
        return mCollection.orderBy("name", Query.Direction.DESCENDING);
    }

    public Query getGroupByUser(String id) {
        return mCollection.whereEqualTo("idUser", id);
    }

    public Query getGroupByName(String name) {
        return mCollection.orderBy("name").startAt(name).endAt(name+'\uf8ff');
    }

    public Task<DocumentSnapshot> getGroupById (String id) {
        return mCollection.document(id).get();
    }

    public Task<Void> delete (String id) {
        return mCollection.document(id).delete();
    }
}
