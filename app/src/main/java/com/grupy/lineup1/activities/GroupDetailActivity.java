package com.grupy.lineup1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.grupy.lineup1.R;
import com.grupy.lineup1.adapters.SliderAdapter;
import com.grupy.lineup1.models.SliderItem;
import com.grupy.lineup1.providers.AuthProvider;
import com.grupy.lineup1.providers.PostProvider;
import com.grupy.lineup1.providers.UserProvider;
import com.grupy.lineup1.utils.ViewedMessageHelper;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupDetailActivity extends AppCompatActivity {

    SliderView mSliderView;
    SliderAdapter mSliderAdapter;
    List<SliderItem> mSliderItems = new ArrayList<>();
    PostProvider mPostProvider;
    UserProvider mUserProvider;

    String mExtraPostId;

    TextView mTextViewName;
    TextView mTextViewDescription;
    TextView mTextViewUsername;
    TextView mTextViewNameCategory;
    ImageView mImageViewCategory;
    CircleImageView mCircleImageViewProfile;
    Button mButtonShowProfile;
    FloatingActionButton mFabChat;
    AuthProvider mAuthProvider;

    CircleImageView arrowBack;

    String mIdUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        mPostProvider = new PostProvider();
        mUserProvider = new UserProvider();

        mSliderView = findViewById(R.id.imageSlider);
        mTextViewName = findViewById(R.id.textViewName);
        mTextViewDescription = findViewById(R.id.textViewDescription);
        mTextViewUsername = findViewById(R.id.textViewUsername);
        mTextViewNameCategory = findViewById(R.id.textViewCategoryName);
        mImageViewCategory = findViewById(R.id.imageViewCategory);
        mCircleImageViewProfile = findViewById(R.id.circleImageViewProfile);
        mButtonShowProfile = findViewById(R.id.btnShowProfile);
        mFabChat = findViewById(R.id.fabChat);
        mAuthProvider = new AuthProvider();
        arrowBack = findViewById(R.id.arrowBack);

        mExtraPostId = getIntent().getStringExtra("id");

        getGroup();

        mButtonShowProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToShowProfile();
            }
        });

        mFabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChatActivity();
            }
        });

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void goToChatActivity() {
        Intent intent = new Intent(GroupDetailActivity.this, ChatActivity.class);
        //mAuthProvider.getUid() with authProvider we obtain users id
        intent.putExtra("idUser1", mAuthProvider.getUid());
        //mIdUser group owner's id
        intent.putExtra("idUser2", mIdUser);
        startActivity(intent);

    }

    private void goToShowProfile() {
        if (!mIdUser.equals("")) {
            Intent intent = new Intent(GroupDetailActivity.this, UserProfileActivity.class);
            intent.putExtra("idUser", mIdUser);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Cannot load user.", Toast.LENGTH_LONG).show();
        }
    }

    private void instanceSlider() {
        mSliderAdapter = new SliderAdapter(GroupDetailActivity.this, mSliderItems);
        mSliderView.setSliderAdapter(mSliderAdapter);
        mSliderView.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM);
        mSliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        mSliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        mSliderView.setIndicatorSelectedColor(Color.WHITE);
        mSliderView.setIndicatorUnselectedColor(Color.GRAY);
        mSliderView.setScrollTimeInSec(3);
        mSliderView.setAutoCycle(false);
    }

    //get the information of the one who created the group
    private void getGroup() {
        mPostProvider.getGroupById(mExtraPostId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    for (int i = 1; i < 7; i++) {
                        if (documentSnapshot.contains("image".concat(String.valueOf(i)))) {
                            if (documentSnapshot.get("image".concat(String.valueOf(i))) != "") {
                                String image = documentSnapshot.getString("image".concat(String.valueOf(i)));
                                SliderItem item = new SliderItem();
                                item.setImageUrl(image);
                                mSliderItems.add(item);
                            }
                        }
                    }
                    if (documentSnapshot.contains("name")) {
                        String name = documentSnapshot.getString("name");
                        mTextViewName.setText(name.toUpperCase());
                    }
                    if (documentSnapshot.contains("description")) {
                        String description = documentSnapshot.getString("description");
                        mTextViewDescription.setText(description);
                    }
                    if (documentSnapshot.contains("idUser")) {
                        mIdUser = documentSnapshot.getString("idUser");
                        getUserInfo(mIdUser);
                    }
                    if (mIdUser.equals(mAuthProvider.getUid())) {
                        mFabChat.setVisibility(View.GONE);
                    }
                    else {
                        mFabChat.setVisibility(View.VISIBLE);
                    }
                    instanceSlider();
                }
            }
        });
    }

    private void getUserInfo(String idUser) {
        mUserProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("username")) {
                        String username = documentSnapshot.getString("username");
                        mTextViewUsername.setText(username);
                    }
                    if (documentSnapshot.contains("image_profile")) {
                        String imageProfile = documentSnapshot.getString("image_profile");
                        if (imageProfile != null && imageProfile != "") {
                            Picasso.with(GroupDetailActivity.this).load(imageProfile).into(mCircleImageViewProfile);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ViewedMessageHelper.updateOnline(true, GroupDetailActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewedMessageHelper.updateOnline(false, GroupDetailActivity.this);
    }
}