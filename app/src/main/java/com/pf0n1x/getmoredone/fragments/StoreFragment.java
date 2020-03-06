package com.pf0n1x.getmoredone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pf0n1x.getmoredone.R;
import com.pf0n1x.getmoredone.adapters.StoreListAdapter;
import com.pf0n1x.getmoredone.entities.Account;
import com.pf0n1x.getmoredone.entities.StoreItem;

import java.util.LinkedList;
import java.util.List;

public class StoreFragment extends Fragment {

    // Data Members
    private TextView mMoneyTextView;
    private Account mCurUser;
    private StoreListAdapter mAdapter;
    private ChildEventListener mStoreItemEventListener;
    private List<StoreItem> mStoreItemList;
    private DatabaseReference mStoreItemsRef;

    // Constant Members
    private final FirebaseDatabase mDb = FirebaseDatabase.getInstance();
    private final DatabaseReference mUserDBRef = mDb
            .getReference("users").child(FirebaseAuth.getInstance()
                    .getCurrentUser().getUid());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        // Initialize basic data.
        mStoreItemList = new LinkedList<StoreItem>();
        mAdapter = new StoreListAdapter(this.getContext());
        RecyclerView recyclerStoreView = view.findViewById(R.id.recyclerview_store_items);
        recyclerStoreView.setAdapter(mAdapter);

        // Set the layout manager.
        LinearLayoutManager listLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerStoreView.setLayoutManager(listLayoutManager);

        // Set the list children behaviour.
        mStoreItemEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                StoreItem newStoreItem = dataSnapshot.getValue(StoreItem.class);
                mStoreItemList.add(newStoreItem);
                mAdapter.setStoreItems(mStoreItemList);
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

        mStoreItemsRef = mDb.getReference("storeItems");
        mStoreItemsRef.orderByChild("start_date")
                .addChildEventListener(mStoreItemEventListener);

        // Get the layout elements.
        mMoneyTextView = view.findViewById(R.id.textview_store_money);

        // Set the user update behaviour.
        setUserUpdateBehaviour();

        return view;
    }

    private void setUserUpdateBehaviour() {
        ValueEventListener accountEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mCurUser = dataSnapshot.getValue(Account.class);
                mAdapter.setCurUser(mCurUser);
                mMoneyTextView.setText(mCurUser.getMoney() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mUserDBRef.addValueEventListener(accountEventListener);
    }
}
