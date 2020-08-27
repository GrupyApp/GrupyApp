package com.grupy.grupy.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.grupy.grupy.R;
import com.grupy.grupy.activities.EditProfileActivity;
import com.grupy.grupy.activities.MainActivity;
import com.grupy.grupy.adapters.MyPostAdapter;
import com.grupy.grupy.adapters.PostAdapter;
import com.grupy.grupy.models.Post;
import com.grupy.grupy.providers.AuthProvider;
import com.grupy.grupy.providers.PostProvider;
import com.grupy.grupy.providers.UserProvider;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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
    TextView mTextViewGroupExist;
    ImageView mImageViewCover;
    CircleImageView mCircleImageViewProfile;

    RecyclerView mRecyclerView;
    Toolbar mToolbar;

    AuthProvider mAuthProvider;
    UserProvider mUserProvider;
    PostProvider mPostProvider;

    MyPostAdapter mMyPostAdapter;

    ListenerRegistration mListener;

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
        mTextViewGroupExist = mView.findViewById(R.id.textViewGroupExist);
        mImageViewCover = mView.findViewById(R.id.imageViewCover);
        mCircleImageViewProfile = mView.findViewById(R.id.circleImageViewProfile);

        mToolbar = mView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");

        mRecyclerView = mView.findViewById(R.id.recyclerViewMyGroup);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

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
        checkIfExistGroup();

        return mView;
    }

    private void checkIfExistGroup() {
        mListener = mPostProvider.getGroupByUser(mAuthProvider.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if (queryDocumentSnapshots != null) {
                    if (mAuthProvider.getUserSession() != null) {
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
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mPostProvider.getGroupByUser(mAuthProvider.getUid());
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();
        mMyPostAdapter = new MyPostAdapter(options, getContext());
        mRecyclerView.setAdapter(mMyPostAdapter);
        mMyPostAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMyPostAdapter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mListener.remove();
        }
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