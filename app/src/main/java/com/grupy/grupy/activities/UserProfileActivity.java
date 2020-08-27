package com.grupy.grupy.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.grupy.grupy.R;
import com.grupy.grupy.adapters.MyPostAdapter;
import com.grupy.grupy.models.Post;
import com.grupy.grupy.models.User;
import com.grupy.grupy.providers.AuthProvider;
import com.grupy.grupy.providers.PostProvider;
import com.grupy.grupy.providers.UserProvider;
import com.grupy.grupy.utils.ViewedMessageHelper;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    TextView mTextViewUsername;
    TextView mTextViewGroupNumber;
    ImageView mImageViewCover;
    CircleImageView mCircleImageViewProfile;
    RecyclerView mRecyclerView;
    Toolbar mToolbar;

    AuthProvider mAuthProvider;
    UserProvider mUserProvider;
    TextView mTextViewGroupExist;
    PostProvider mPostProvider;

    MyPostAdapter mMyPostAdapter;

    ListenerRegistration mListener;

    String mExtraIdUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mTextViewUsername = findViewById(R.id.textViewUsername);
        mTextViewGroupNumber = findViewById(R.id.textViewGroupNumber);
        mTextViewGroupExist = findViewById(R.id.textViewGroupExist);
        mImageViewCover = findViewById(R.id.imageViewCover);
        mCircleImageViewProfile = findViewById(R.id.circleImageViewProfile);


        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //Back arrow

        mRecyclerView = findViewById(R.id.recyclerViewMyGroup);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserProfileActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mExtraIdUser = getIntent().getStringExtra("idUser");

        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();
        mPostProvider = new PostProvider();



        getUser();
        getGroupNumber();
        checkIfExistGroup();
    }



    @Override
    public void onStart() {
        super.onStart();
        Query query = mPostProvider.getGroupByUser(mExtraIdUser);
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();
        mMyPostAdapter = new MyPostAdapter(options, UserProfileActivity.this);
        mRecyclerView.setAdapter(mMyPostAdapter);
        mMyPostAdapter.startListening();
        ViewedMessageHelper.updateOnline(true, UserProfileActivity.this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mMyPostAdapter.stopListening();
        ViewedMessageHelper.updateOnline(false, UserProfileActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mListener.remove();
        }
    }

    private void checkIfExistGroup() {
        mListener = mPostProvider.getGroupByUser(mExtraIdUser).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if (queryDocumentSnapshots != null) {
                    int numberGroup = queryDocumentSnapshots.size();
                    if (numberGroup > 0) {
                        mTextViewGroupExist.setText("Groups");
                        mTextViewGroupExist.setTextColor(Color.GRAY);
                    }
                    else {
                        mTextViewGroupExist.setText("No groups");
                        mTextViewGroupExist.setTextColor(Color.GRAY);
                    }
                }
            }
        });
    }

    private void getUser() {
        mUserProvider.getUser(mExtraIdUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("username")) {
                        String username = documentSnapshot.getString("username");
                        mTextViewUsername.setText(username);
                    }
                }
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("image_profile")) {
                        String image_profile = documentSnapshot.getString("image_profile");
                        if (image_profile != null) {
                            if (!image_profile.isEmpty()) {
                                Picasso.with(UserProfileActivity.this).load(image_profile).into(mCircleImageViewProfile);
                            }
                        }
                    }
                }
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("image_cover")) {
                        String image_cover = documentSnapshot.getString("image_cover");
                        if (image_cover != null) {
                            if (!image_cover.isEmpty()) {
                                Picasso.with(UserProfileActivity.this).load(image_cover).into(mImageViewCover);
                            }
                        }
                    }
                }
            }
        });
    }

    private void getGroupNumber() {
        mPostProvider.getGroupByUser(mExtraIdUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberGroup = queryDocumentSnapshots.size();
                mTextViewGroupNumber.setText(String.valueOf(numberGroup));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}