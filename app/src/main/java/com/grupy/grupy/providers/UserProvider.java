package com.grupy.grupy.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.grupy.grupy.models.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserProvider {

    private CollectionReference mCollection;

    public UserProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("Users");
    }

    public Task<DocumentSnapshot> getUser(String id) {
        return mCollection.document(id).get();
    }

    public Task<Void> create(User user) {
        return mCollection.document(user.getId()).set(user);
    }

    public Task<Void> update(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("timestamp", new Date().getTime());
        map.put("image_profile", user.getImageProfile());
        map.put("image_cover", user.getImageCover());
        return mCollection.document(user.getId()).update(map);
    }

}

