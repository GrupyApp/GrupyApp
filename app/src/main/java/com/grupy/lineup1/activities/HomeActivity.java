package com.grupy.lineup1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.grupy.lineup1.R;
import com.grupy.lineup1.fragments.ChatFragment;
import com.grupy.lineup1.fragments.HomeFragment;
import com.grupy.lineup1.fragments.ProfileFragment;
import com.grupy.lineup1.providers.AuthProvider;
import com.grupy.lineup1.providers.TokenProvider;
import com.grupy.lineup1.providers.UserProvider;
import com.grupy.lineup1.utils.ViewedMessageHelper;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    TokenProvider mTokenProvider;
    AuthProvider mAuthProvider;
    UserProvider mUserProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        mTokenProvider = new TokenProvider();
        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();

        openFragment(new HomeFragment());
        createToken();
        Toast.makeText(HomeActivity.this, "Token", Toast.LENGTH_LONG);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ViewedMessageHelper.updateOnline(true, HomeActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewedMessageHelper.updateOnline(false, HomeActivity.this);
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.itemHome) {
                        //FRAGMENT HOME
                        openFragment(new HomeFragment());
                    }
                    else if (item.getItemId() == R.id.itemProfile) {
                        //FRAGMENT PROFILE
                        openFragment(new ProfileFragment());
                    }
                    else if (item.getItemId() == R.id.itemChat) {
                        //FRAGMENT PROFILE
                        openFragment(new ChatFragment());
                    }
                    return true;
                }
            };

    private void createToken() {
        mTokenProvider.create(mAuthProvider.getUid());
        Toast.makeText(HomeActivity.this, "Token", Toast.LENGTH_LONG);
    }
}