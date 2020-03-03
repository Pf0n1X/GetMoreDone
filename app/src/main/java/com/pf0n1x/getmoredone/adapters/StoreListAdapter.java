package com.pf0n1x.getmoredone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pf0n1x.getmoredone.BR;
import com.pf0n1x.getmoredone.R;
import com.pf0n1x.getmoredone.entities.StoreItem;

import java.util.List;

public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.StoreItemViewHolder> {

    // Data Members
    private List<StoreItem> mStoreItems;
    private Context mContext;

    // Constant Members
    private final FirebaseDatabase mDb = FirebaseDatabase.getInstance();
    private final DatabaseReference mUserDBRef = mDb
            .getReference("storeItems");
    private final LayoutInflater mInflater;

    // Constructors
    public StoreListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @NonNull
    @Override
    public StoreListAdapter.StoreItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(mInflater, R.layout.store_item, parent, false);

        return new StoreItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreListAdapter.StoreItemViewHolder holder, int position) {
        if (mStoreItems != null) {
            StoreItem current = mStoreItems.get(position);
            holder.bind(current);
        } else {

            // Covers the case of data not being ready yet.
            // holder.taskItemTitleView.setText("No tasks yet... :)"); // TODO: Add and make a real text
        }
    }

    @Override
    public int getItemCount() {
        if (mStoreItems != null) {
            return mStoreItems.size();
        } else {
            return 0;
        }
    }

    public void setStoreItems(List<StoreItem> storeItems) {
        this.mStoreItems = storeItems;
        notifyDataSetChanged();
    }

    public class StoreItemViewHolder extends RecyclerView.ViewHolder {

        // Data Members
        private ViewDataBinding dataBinding;
        private TextView titleTextView;
        private TextView descTextView;
        private ImageView imgImageView;

        public StoreItemViewHolder(final ViewDataBinding dataBinding) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;

            // Initialize the fragment components references.
            this.titleTextView = itemView.findViewById(R.id.store_item_title);
            this.descTextView = itemView.findViewById(R.id.store_item_desc);
            this.imgImageView = itemView.findViewById(R.id.store_item_img);
        }

        public void bind(StoreItem current) {
            this.dataBinding.setVariable(BR.store_item, current);
            this.dataBinding.executePendingBindings();

            StoreItem curStoreItem = mStoreItems.get(getAdapterPosition());

            // Set the according to the item.
            switch (curStoreItem.getId()) {
                case "0":
                    this.titleTextView.setText(R.string.store_item_streak_freeze_title);
                    this.descTextView.setText(R.string.store_item_streak_freeze_desc);
                    this.imgImageView.setImageResource(R.drawable.icons8_winter_500);
                    break;
                case "1":
                    this.titleTextView.setText(R.string.store_item_wager_title);
                    this.descTextView.setText(R.string.store_item_wager_desc);
                    this.imgImageView.setImageResource(R.drawable.icons8_fire_500);
                    break;
            }
        }
    }
}
