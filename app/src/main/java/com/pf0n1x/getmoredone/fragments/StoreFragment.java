package com.pf0n1x.getmoredone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pf0n1x.getmoredone.R;
import com.pf0n1x.getmoredone.entities.Account;

import java.util.Calendar;

public class StoreFragment extends Fragment {

    // Data Members
    private TextView mMoneyTextView;
    private Account mCurUser;

    // Constant Members
    private final FirebaseDatabase mDb = FirebaseDatabase.getInstance();
    private final DatabaseReference mUserDBRef = mDb
            .getReference("users").child(FirebaseAuth.getInstance()
                    .getCurrentUser().getUid());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);

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
                mMoneyTextView.setText(mCurUser.getMoney() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mUserDBRef.addValueEventListener(accountEventListener);
    }
}
