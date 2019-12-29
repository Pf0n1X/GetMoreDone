package com.pf0n1x.getmoredone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.pf0n1x.getmoredone.BR;
import com.pf0n1x.getmoredone.R;
import com.pf0n1x.getmoredone.entities.Achievement;
import com.skydoves.progressview.ProgressView;

import java.util.List;

public class AchievementListAdapter
        extends RecyclerView.Adapter<AchievementListAdapter.AchievementViewHolder> {

    // Data Members
    private final LayoutInflater mInflater;
    private List<Achievement> mAchievements; // A cached copy of the achievement list
    private static AchievementListAdapter.ClickListener clickListener;

    // Constant Members
    private final FirebaseDatabase mDb = FirebaseDatabase.getInstance();

    // Constructors
    public AchievementListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void setAchievements(List<Achievement> achievements) {
        this.mAchievements = achievements;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(mInflater, R.layout.achievement, parent, false);
        return new AchievementViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        if (mAchievements != null) {
            Achievement current = mAchievements.get(position);
            holder.bind(current, position);
        } else {

            // Covers the case of data not being ready yet.
//            holder.taskItemTitleView.setText("No tasks yet... :)"); // TODO: Add and make a real text
        }
    }

    @Override
    public int getItemCount() {
        if (mAchievements != null) {
            return mAchievements.size();
        } else {
            return 0;
        }
    }

    class AchievementViewHolder extends RecyclerView.ViewHolder {

        // Data Members
        private ViewDataBinding dataBinding;
        private ImageView itemAchImg;
        private ProgressView itemProgressView;
        private TextView itemTextView;

        private AchievementViewHolder(final ViewDataBinding dataBinding) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;
            itemAchImg = dataBinding.getRoot().findViewById(R.id.imageview_ach_icon);
            itemProgressView = dataBinding.getRoot().findViewById(R.id.progressview_achievement);
            itemTextView = dataBinding.getRoot().findViewById(R.id.textview_ach_name);
        }

        public void bind(Achievement current, int position) {
            this.dataBinding.setVariable(BR.achievement, current);
            this.dataBinding.executePendingBindings();

            switch (current.getId()) {

                // Streak achievement
                case "1": // TODO: change to a constant
                    itemAchImg.setImageResource(R.drawable.icons8_lightning_bolt_500);
                    itemTextView.setText(dataBinding.getRoot()
                            .getResources()
                            .getString(R.string.ach_streak_text, current.getCurrentStarMaxProgress()));
                    break;

                // Money achievement
                case "2": // TODO: change to a constant
                    itemAchImg.setImageResource(R.drawable.icons8_money_box_500);
                    itemTextView.setText(dataBinding.getRoot()
                            .getResources()
                            .getString(R.string.ach_money_text, current.getCurrentStarMaxProgress()));
                    break;
                default:
            }

            itemProgressView.setProgress(current.getCurrentStarProgress());
            itemProgressView.setMax(current.getCurrentStarMaxProgress());
            itemProgressView
                    .setLabelText(current.getCurrentStarProgress() + " / " + current.getCurrentStarMaxProgress());
        }
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}
