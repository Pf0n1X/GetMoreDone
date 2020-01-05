package com.pf0n1x.getmoredone.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.pf0n1x.getmoredone.BR;
import com.pf0n1x.getmoredone.R;
import com.pf0n1x.getmoredone.entities.Account;
import com.pf0n1x.getmoredone.entities.Task;
import java.util.List;

// TODO: Add task deletion.
// TODO: Add documentation.
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

    // Data Members
    private final LayoutInflater mInflater;
    private List<Task> mTasks; // A cached copy of the tasks
    private Context mContext;
    private Account mCurUser;

    // Constant Members
    private final FirebaseDatabase mDb = FirebaseDatabase.getInstance();
    private final DatabaseReference mUserDBRef = mDb
            .getReference("users").child(FirebaseAuth.getInstance()
                    .getCurrentUser().getUid());

    // Constructors
    public TaskListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(mInflater, R.layout.task, parent, false);
        return new TaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        if (mTasks != null) {
            Task current = mTasks.get(position);
            holder.bind(current);
        } else {

            // Covers the case of data not being ready yet.
//            holder.taskItemTitleView.setText("No tasks yet... :)"); // TODO: Add and make a real text
        }
    }

    @Override
    public int getItemCount() {
        if (mTasks != null) {
            return mTasks.size();
        } else {
            return 0;
        }
    }

    public void setTasks(List<Task> tasks) {
        this.mTasks = tasks;
        notifyDataSetChanged();
    }

    public void setCurUser(Account user) {
        this.mCurUser = user;
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {

        // Data Members
        private final CheckBox taskItemCheckBox;
        private ViewDataBinding dataBinding;

        private TaskViewHolder(final ViewDataBinding dataBinding) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;
            taskItemCheckBox = itemView.findViewById(R.id.checkBox);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    TextView titleTextView = v.getRootView().findViewById(R.id.single_task_title);
                    TextView descTextView = v.getRootView().findViewById(R.id.single_task_desc);
                    titleTextView.setText(mTasks.get(getAdapterPosition()).getTitle());
                    descTextView.setText(mTasks.get(getAdapterPosition()).getDescription());
                }
            });

            taskItemCheckBox.setOnClickListener(new CheckBox.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Get the checkbox current state
                    boolean isChecked = taskItemCheckBox.isChecked();
                    Task curTask = mTasks.get(getAdapterPosition());
                    curTask.setIs_done(isChecked);
                    int progress = getCompletedTasksCount();

                    if (mTasks.size() == progress && curTask.getIs_done()) {

                        new LottieAlertDialog.Builder(mContext, DialogTypes.TYPE_SUCCESS)
                                .setTitle("Daily goal achieved!") // TODO: Extract text resource
                                .setDescription("You have achieved your daily goal," +
                                        "\nwhich is why you will be rewarded with 50 coins.") // TODO: Extract text resource
                                .build()
                                .show();


                        // Update the user's money.
                        mCurUser.setMoney(mCurUser.getMoney() + 50);
                        mUserDBRef.setValue(mCurUser);
                    } else if (curTask.getIs_done()) {
                        new LottieAlertDialog.Builder(mContext, DialogTypes.TYPE_SUCCESS)
                                .setTitle("Good job!") // TODO: Extract text resource
                                .setDescription("You're the best!") // TODO: Extract text resource
                                .build()
                                .show();
                    }

                    // Update the entity accordingly.
                    mTasks.get(getAdapterPosition()).setIs_done(isChecked);

                    // Update the entity in the DB
                    DatabaseReference usersDBRef =
                            mDb.getReference
                                    ("user_collections/"
                                            + FirebaseAuth
                                            .getInstance()
                                            .getCurrentUser()
                                            .getUid() + "/tasks");
                    usersDBRef
                            .child(mTasks.get(getAdapterPosition()).getId())
                            .child("is_done")
                            .setValue(mTasks.get(getAdapterPosition()).getIs_done());
                }
            });
        }

        public void bind(Task current) {
            this.dataBinding.setVariable(BR.task, current);
            this.dataBinding.executePendingBindings();
        }
    }

    public int getCompletedTasksCount() {
        int count = 0;

        if (mTasks != null) {

            // Loop through all the tasks and calculate their sum
            for (int cur = 0; cur < mTasks.size(); cur++) {
                if (mTasks.get(cur).getIs_done()) {
                    count++;
                }
            }
        }

        return count;
    }

//    public interface ClickListener {
//        void onItemClick(View v, int position);
//    }
}
