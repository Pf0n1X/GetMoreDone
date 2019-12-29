package com.pf0n1x.getmoredone.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pf0n1x.getmoredone.MainActivity;
import com.pf0n1x.getmoredone.R;
import com.pf0n1x.getmoredone.SettingsActivity;
import com.pf0n1x.getmoredone.adapters.AchievementListAdapter;
import com.pf0n1x.getmoredone.adapters.LeaderboardListAdapter;
import com.pf0n1x.getmoredone.entities.Account;
import com.pf0n1x.getmoredone.entities.Achievement;

import java.util.Collections;
import java.util.LinkedList;

public class ProfileFragment extends Fragment {

    // Data Members
    private TextView mNameTextView;
    private LinkedList<Achievement> mAchievements;
    private AchievementListAdapter mAdapter;
    private ChildEventListener mLBEventListner;
    private FirebaseDatabase mDb;
    private DatabaseReference mAchRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the fragment.
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Prepare the adapter data
        mAchievements = new LinkedList<Achievement>();
        mAdapter = new AchievementListAdapter(this.getContext());

        // TODO: Get user's name.
        mNameTextView = view.findViewById(R.id.textview_name);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        mNameTextView.setText(auth.getCurrentUser().getDisplayName());

        // Set the fragment's toolbar.
        Toolbar toolbar = view.findViewById(R.id.toolbar_profile);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        // Prepare the recycler view.
        RecyclerView recyclerLBView = view.findViewById(R.id.recyclerview_achievements);
        recyclerLBView.setAdapter(mAdapter);
        LinearLayoutManager listLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerLBView.setLayoutManager(listLayoutManager);
        recyclerLBView.addItemDecoration(new DividerItemDecoration(this.getContext(),
                listLayoutManager.getOrientation()));

        // Prepare the event listener.
        mLBEventListner = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Achievement newAchievement = dataSnapshot.getValue(Achievement.class);
                mAchievements.add(newAchievement);
                mAdapter.setAchievements(mAchievements);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        // Get the leaderboard data from the db.
        mDb = FirebaseDatabase.getInstance();
        mAchRef = mDb.getReference("user_collections/"
                + FirebaseAuth.getInstance().getCurrentUser().getUid()
                + "/achievements");
        mAchRef.addChildEventListener(mLBEventListner);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Get the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_profile_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*
        This method handles the toolbar menu selection.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(getContext(), SettingsActivity.class);
                startActivity(settingsIntent);

                return true;

            default:
                // Do nothing.
        }

        return super.onOptionsItemSelected(item);
    }
}
