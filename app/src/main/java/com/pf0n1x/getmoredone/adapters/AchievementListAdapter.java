package com.pf0n1x.getmoredone.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AchievementListAdapter
        extends RecyclerView.Adapter<AchievementListAdapter.AchievementViewHolder> {

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class AchievementViewHolder extends RecyclerView.ViewHolder {
        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
