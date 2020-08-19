package com.grupy.grupy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.grupy.grupy.R;
import com.grupy.grupy.adapters.SliderAdapter;
import com.grupy.grupy.models.SliderItem;
import com.grupy.grupy.providers.PostProvider;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailActivity extends AppCompatActivity {

    SliderView mSliderView;
    SliderAdapter mSliderAdapter;
    List<SliderItem> mSliderItems = new ArrayList<>();
    PostProvider mPostProvider;

    String mExtraPostId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        mPostProvider = new PostProvider();

        mSliderView = findViewById(R.id.imageSlider);

        mExtraPostId = getIntent().getStringExtra("id");

        getGroup();
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
        mSliderView.setAutoCycle(true);
        mSliderView.startAutoCycle();
    }

    private void getGroup() {
        mPostProvider.getGroupById(mExtraPostId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("image")) {
                        String image1 = documentSnapshot.getString("image");
                        SliderItem item = new SliderItem();
                        item.setImageUrl(image1);
                        mSliderItems.add(item);
                    }
                    instanceSlider();
                }
            }
        });
    }
}