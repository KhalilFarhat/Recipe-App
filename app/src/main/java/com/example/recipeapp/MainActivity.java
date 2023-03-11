package com.example.recipeapp;

import android.os.Bundle;

import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // Set a listener for when menu items are selected
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Handle navigation view item clicks here
                int id = menuItem.getItemId();

                if (id == R.id.nav_home) {
                    // Switch to home fragment or activity
                } else if (id == R.id.nav_search) {
                    // Switch to search fragment or activity
                } else if (id == R.id.nav_account) {
                    // Switch to account fragment or activity
                } else if (id == R.id.nav_settings) {
                    // Switch to settings fragment or activity
                }

                // Close the navigation drawer
                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });
    }

    // Override onBackPressed to close the drawer if it's open
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
