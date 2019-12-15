package com.pf0n1x.getmoredone.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.resources.TextAppearance;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pf0n1x.getmoredone.R;
import com.pf0n1x.getmoredone.entities.Task;
import java.util.List;

// TODO: Add task deletion.
// TODO: Add documentation.
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

    // Data Members
    private final LayoutInflater mInflater;
    private List<Task> mTasks; // A cached copy of the tasks
    private static ClickListener clickListener;

    // Constant Members
    private final FirebaseDatabase mDb = FirebaseDatabase.getInstance();

    // Constructors
    public TaskListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.task, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        if (mTasks != null) {
            Task current = mTasks.get(position);
            holder.taskItemTitleView.setText(current.getTitle()); // TODO: Update the rest of the item's attributes.
            holder.taskItemSubTitleView.setText(current.getDescription());
            holder.taskItemCheckBox.setChecked(current.getIs_done());

            // Set the task as checked.
            if (current.getIs_done()) {
                holder.taskItemTitleView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.taskItemTitleView.setPaintFlags(0);
            }
        } else {

            // Covers the case of data not being ready yet.
            holder.taskItemTitleView.setText("No tasks yet... :)"); // TODO: Add and make a real text
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

    class TaskViewHolder extends RecyclerView.ViewHolder {

        // Data Members
        private final TextView taskItemTitleView;
        private final TextView taskItemSubTitleView; // TODO: Add the rest of the item's attributes
        private final CheckBox taskItemCheckBox;

        private TaskViewHolder(View itemView) {
            super(itemView);
            taskItemTitleView = itemView.findViewById(R.id.task_title);
            taskItemSubTitleView = itemView.findViewById(R.id.task_sub_title);
            taskItemCheckBox = itemView.findViewById(R.id.checkBox);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v, getAdapterPosition());
                }
            });

            taskItemCheckBox.setOnClickListener(new CheckBox.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Get the checkbox current state
                    boolean isChecked = taskItemCheckBox.isChecked();

                    // Set the strikethrough according to the checkbox.
                    if (isChecked) {
                        taskItemTitleView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        taskItemTitleView.setPaintFlags(0);
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

                    // Notify the RecyclerView about the update.
                    notifyDataSetChanged();
                }
            });
        }

    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}
