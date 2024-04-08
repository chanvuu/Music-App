package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;


import com.example.musicapp.Fragment.HomeFragment;
import com.example.musicapp.Fragment.SearchFragment;
import com.example.musicapp.Service.Authentication;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentContainer = findViewById(R.id.fragmentContainer);

        // Initially, load your default fragment
//        loadFragment(new DefaultFragment());

        // Set up bottom navigation item click listener
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnItemSelectedListener(item -> {
            // Switch based on item IDs directly
            switch (item.getItemId()) {
                case R.id.home:
                    // Handle Home item click
                    // Replace the current fragment with the home fragment
//                        loadFragment(new HomeFragment());
                    loadFragment(new HomeFragment());
                    return true;
                case R.id.search:
                    // Handle Search item click
                    // Replace the current fragment with the search fragment
                    loadFragment(new SearchFragment());
                    return true;
                case R.id.library:
                    // Handle Library item click
                    // Replace the current fragment with the library fragment
//                        loadFragment(new LibraryFragment());
                    Intent intent = new Intent(MainActivity.this, Authentication.class);
                    startActivity(intent);
                    return true;
                default:
                    return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainer.getId(), fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}

