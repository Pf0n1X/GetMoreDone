package com.pf0n1x.getmoredone.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.pf0n1x.getmoredone.R;
import com.pf0n1x.getmoredone.adapters.LeaderboardListAdapter;
import com.pf0n1x.getmoredone.entities.Account;

import java.util.Collections;
import java.util.LinkedList;

public class LeaderboardFragment extends Fragment {

    // Data Members
    private LinkedList<Account> mLeaderboard;
    private LeaderboardListAdapter mAdapter;
    private ChildEventListener mLBEventListner;
    private FirebaseDatabase mDb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        mLeaderboard = new LinkedList<Account>();
        mAdapter = new LeaderboardListAdapter(this.getContext());

        // Prepare the recycler view.
        RecyclerView recyclerLBView = view.findViewById(R.id.recyclerview_leaderboard);
        recyclerLBView.setAdapter(mAdapter);
        LinearLayoutManager listLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerLBView.setLayoutManager(listLayoutManager);
        recyclerLBView.addItemDecoration(new DividerItemDecoration(this.getContext(),
                listLayoutManager.getOrientation()));

        // Prepare the event listener.
        mLBEventListner = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Account newAccount = dataSnapshot.getValue(Account.class);
                mLeaderboard.add(newAccount);
                Collections.sort(mLeaderboard);
                mAdapter.setLeaderboard(mLeaderboard);
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
        mDb.getReference("users").addChildEventListener(mLBEventListner);

        return view;
    }

}
