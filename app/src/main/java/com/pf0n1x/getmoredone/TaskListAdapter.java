package com.pf0n1x.getmoredone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.pf0n1x.getmoredone.entities.Task;
import com.pf0n1x.getmoredone.view_models.TaskViewModel;

import java.util.List;

// TODO: Add task deletion.
// TODO: Add documentation.
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

    // Data Members
    private final LayoutInflater mInflater;
    private List<Task> mTasks; // A cached copy of the tasks
    private static ClickListener clickListener; // TODO: Delete unused variable
    private TaskViewModel mTaskViewModel;

    // Constructors
    public TaskListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mTaskViewModel = ViewModelProviders.of((FragmentActivity)context).get(TaskViewModel.class);
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

                    // Update the entity accordingly.
                    mTasks.get(getAdapterPosition()).setIs_done(isChecked); // TODO: Figure out how to change it in the database

                   // Update the entity in the DB
                    mTaskViewModel.updateTask(mTasks.get(getAdapterPosition()));

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
