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
import com.google.firebase.database.FirebaseDatabase;
import com.pf0n1x.getmoredone.R;

public class ProfileFragment extends Fragment {

    // Data Members
    TextView mNameTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the fragment.
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Get user's name.
        mNameTextView = view.findViewById(R.id.textview_name);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        mNameTextView.setText(auth.getCurrentUser().getDisplayName());

        return view;
    }
}
