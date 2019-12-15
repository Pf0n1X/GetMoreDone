package com.pf0n1x.getmoredone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.pf0n1x.getmoredone.R;
import com.pf0n1x.getmoredone.entities.Account;

import java.util.List;

public class LeaderboardListAdapter extends RecyclerView.Adapter<LeaderboardListAdapter.LeaderboardViewHolder> {

    // Data Members
    private final LayoutInflater mInflater;
    private List<Account> mLeaderboard; // A cached copy of the leaderboard
    private static TaskListAdapter.ClickListener clickListener;

    // Constant Members
    private final FirebaseDatabase mDb = FirebaseDatabase.getInstance();

    // Constructors
    public LeaderboardListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setLeaderboard(List<Account> leaderboard) {
        this.mLeaderboard = leaderboard;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.leaderboard_item, parent, false);
        return new LeaderboardListAdapter.LeaderboardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        if (mLeaderboard != null) {
            Account current = mLeaderboard.get(position);
            holder.lbItemPosTextView.setText("" + (position + 1));
            holder.lbItemNameTextView.setText(current.getName());
            holder.lbItemExpTextView.setText(current.getWeeklyExperience() + " XP");
        } else {

            // Covers the case of data not being ready yet.
            holder.lbItemNameTextView.setText("No tasks yet... :)"); // TODO: Add and make a real text
        }
    }

    @Override
    public int getItemCount() {
        if (mLeaderboard != null) {
            return mLeaderboard.size();
        } else {
            return 0;
        }
    }

    class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        // Data Members
        private final TextView lbItemPosTextView;
        private final TextView lbItemNameTextView;
        private final TextView lbItemExpTextView;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the list item elements
            lbItemPosTextView = itemView.findViewById(R.id.textview_lb_pos);
            lbItemNameTextView = itemView.findViewById(R.id.textview_lb_name);
            lbItemExpTextView = itemView.findViewById(R.id.textview_lb_exp);
        }
    }
}
