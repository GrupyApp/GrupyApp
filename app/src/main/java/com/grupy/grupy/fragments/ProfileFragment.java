package com.grupy.grupy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.grupy.grupy.R;
import com.grupy.grupy.activities.EditProfileActivity;
import com.grupy.grupy.activities.MainActivity;
import com.grupy.grupy.providers.AuthProvider;
import com.grupy.grupy.providers.PostProvider;
import com.grupy.grupy.providers.UserProvider;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    View mView;
    Button mButtonFinish;
    LinearLayout mLinearLayoutEditProfile;
    TextView mTextViewUsername;
    TextView mTextViewGroupNumber;
    ImageView mImageViewCover;
    CircleImageView mCircleImageViewProfile;

    AuthProvider mAuthProvider;
    UserProvider mUserProvider;
    PostProvider mPostProvider;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_profile, container, false);

        mTextViewUsername = mView.findViewById(R.id.textViewUsername);
        mTextViewGroupNumber = mView.findViewById(R.id.textViewGroupNumber);
        mImageViewCover = mView.findViewById(R.id.imageViewCover);
        mCircleImageViewProfile = mView.findViewById(R.id.circleImageViewProfile);

        mButtonFinish = mView.findViewById(R.id.btnLogOut);
        mLinearLayoutEditProfile = mView.findViewById(R.id.linearLayoutEditProfile);

        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();
        mPostProvider = new PostProvider();

        mButtonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        mLinearLayoutEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditProfile();
            }
        });

        getUser();
        getGroupNumber();

        return mView;
    }

    private void goToEditProfile() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void getGroupNumber() {
        mPostProvider.getGroupByUser(mAuthProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberGroup = queryDocumentSnapshots.size();
                mTextViewGroupNumber.setText(String.valueOf(numberGroup));
            }
        });
    }

    private void logout() {
        mAuthProvider.logout();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void getUser() {
        mUserProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                                Picasso.with(getContext()).load(image_profile).into(mCircleImageViewProfile);
                            }
                        }
                    }
                }
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("image_cover")) {
                        String image_cover = documentSnapshot.getString("image_cover");
                        if (image_cover != null) {
                            if (!image_cover.isEmpty()) {
                                Picasso.with(getContext()).load(image_cover).into(mImageViewCover);
                            }
                        }
                    }
                }
            }
        });
    }
}