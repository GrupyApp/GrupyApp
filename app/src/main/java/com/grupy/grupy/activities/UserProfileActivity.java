package com.grupy.grupy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.grupy.grupy.R;
import com.grupy.grupy.providers.AuthProvider;
import com.grupy.grupy.providers.PostProvider;
import com.grupy.grupy.providers.UserProvider;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    TextView mTextViewUsername;
    TextView mTextViewGroupNumber;
    ImageView mImageViewCover;
    CircleImageView mCircleImageViewProfile;

    AuthProvider mAuthProvider;
    UserProvider mUserProvider;
    PostProvider mPostProvider;

    String mExtraIdUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mTextViewUsername = findViewById(R.id.textViewUsername);
        mTextViewGroupNumber = findViewById(R.id.textViewGroupNumber);
        mImageViewCover = findViewById(R.id.imageViewCover);
        mCircleImageViewProfile = findViewById(R.id.circleImageViewProfile);

        mExtraIdUser = getIntent().getStringExtra("idUser");

        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();
        mPostProvider = new PostProvider();

        getUser();
        getGroupNumber();
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
}