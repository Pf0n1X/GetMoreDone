package com.pf0n1x.getmoredone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pf0n1x.getmoredone.BR;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
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
        ViewDataBinding binding = DataBindingUtil.inflate(mInflater, R.layout.leaderboard_item, parent, false);
        return new LeaderboardViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        if (mLeaderboard != null) {
            Account current = mLeaderboard.get(position);
            holder.bind(current, position);
        } else {

            // Covers the case of data not being ready yet.
//            holder.lbItemNameTextView.setText("No tasks yet... :)"); // TODO: Add and make a real text
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
        private ViewDataBinding dataBinding;
        private TextView itemPositionTextView;

        private LeaderboardViewHolder(final ViewDataBinding dataBinding) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;
            itemPositionTextView = itemView.getRootView().findViewById(R.id.textview_lb_pos);
        }

        public void bind(Account current, int position) {
            this.dataBinding.setVariable(BR.account, current);
            this.dataBinding.executePendingBindings();
            itemPositionTextView.setText((position + 1) + "");
        }
    }
}
