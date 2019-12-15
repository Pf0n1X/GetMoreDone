package com.pf0n1x.getmoredone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pf0n1x.getmoredone.fragments.LeaderboardFragment;
import com.pf0n1x.getmoredone.fragments.ProfileFragment;
import com.pf0n1x.getmoredone.fragments.StoreFragment;
import com.pf0n1x.getmoredone.fragments.TasksFragment;

import java.util.Arrays;
import java.util.List;

// TODO: Handle CheckBox Tick(DB update, sound, dialog, progress bar update).
// TODO: Add the leaderboard section and fragment.
// TODO: Add the store.
// TODO: Learn & add sounds on mission complete and progress bar fill

public class MainActivity extends AppCompatActivity {

    // Constant Members
    private static final int RC_SIGN_IN = 123;

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
                case R.id.nav_leaderboard:
                    selectedFragment = new LeaderboardFragment();
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

        // TODO: Develop the sign in
        launchSignInActivity();
        setContentView(R.layout.activity_main);
    }

    private void launchSignInActivity() {

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {

                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // Handle the bottom navigation menu
                BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
                bottomNav.setOnNavigationItemSelectedListener(this.mNavListener);

                // Set the default fragment view.
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new TasksFragment())
                        .commit();
                bottomNav.setSelectedItemId(R.id.nav_tasks);
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error
            }
        }
    }
}
