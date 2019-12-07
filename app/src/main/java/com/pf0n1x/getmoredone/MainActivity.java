package com.pf0n1x.getmoredone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pf0n1x.getmoredone.fragments.ProfileFragment;
import com.pf0n1x.getmoredone.fragments.StoreFragment;
import com.pf0n1x.getmoredone.fragments.TasksFragment;

// TODO: Handle CheckBox Tick(DB update, sound, dialog, progress bar update).
// TODO: Fix the colors.
// TODO: Add the ability to add a task.
// TODO: Add the leaderboard section and fragment.
// TODO: Add the store.
// TODO: Learn & add sounds on mission complete and progress bar fill

public class MainActivity extends AppCompatActivity {

    // Data Members
    private BottomNavigationView.OnNavigationItemSelectedListener mNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            // Find which navigation item was selected.
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {
                case R.id.nav_tasks:
                    selectedFragment = new TasksFragment();
                    break;
                case R.id.nav_store:
                    selectedFragment = new StoreFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }

            // Replace the fragment container with the selected fragment.
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handle the bottom navigation menu
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(this.mNavListener);

        // Set the default fragment view.
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new TasksFragment())
                .commit();
        bottomNav.setSelectedItemId(R.id.nav_tasks);
    }
}
