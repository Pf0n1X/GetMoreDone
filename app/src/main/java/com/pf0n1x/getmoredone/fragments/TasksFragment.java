package com.pf0n1x.getmoredone.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pf0n1x.getmoredone.NewTaskActivity;
import com.pf0n1x.getmoredone.R;
import com.pf0n1x.getmoredone.TaskListAdapter;
import com.pf0n1x.getmoredone.entities.Task;
import com.pf0n1x.getmoredone.view_models.TaskViewModel;
import com.skydoves.progressview.ProgressView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TasksFragment extends Fragment {

    // Data Members
    private TaskViewModel mTaskViewModel;
    private ProgressView mProgressView;
    private FloatingActionButton mFABNewTask;
    private CheckBox mTaskCheckBox; // TODO: Delete if it is unused
    private Context mContext;
    private Date mCurShownDate;
    private Observer<List<Task>> mTaskListObserver;
    private DatePickerDialog mCurDatePickerDialog;
    private LiveData<List<Task>> mTasks;

    // Constant Members
    private final Calendar mCalendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the fragment.
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        // Save the progress view.
        mProgressView = view.findViewById(R.id.progressViewTasks); // TODO: Define a setter
        mFABNewTask = view.findViewById(R.id.fab_new_task); // TODO: Define a setter
        mTaskCheckBox = view.findViewById(R.id.checkBox);
        mContext = this.getContext();

        RecyclerView recyclerTasksView = view.findViewById(R.id.recyclerview_tasks);
        final TaskListAdapter adapter = new TaskListAdapter(this.getContext());
        recyclerTasksView.setAdapter(adapter);
        LinearLayoutManager listLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerTasksView.setLayoutManager(listLayoutManager);
        recyclerTasksView.addItemDecoration(new DividerItemDecoration(this.getContext(),
                listLayoutManager.getOrientation()));
        mCurShownDate = new Date(System.currentTimeMillis());
        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        mTaskListObserver = new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable final List<Task> tasks) {

                // Update the cached copy of the words in the adapter.
                adapter.setTasks(tasks);

                // Get the current progress
                float progress = calculateProgress(tasks);

                // Set the percentage according to the
                // user's progress
                mProgressView.setProgress(progress);
                mProgressView.setLabelText((int)progress + "% achieved.");
            }
        };

        // TODO: Check why it is called twice.
        this.mTasks = mTaskViewModel.getTasksOfDate(mCurShownDate); // Define a setter for mTasks
        this.mTasks.observe(this, mTaskListObserver);

        // Set the onClick event for the new task activity.
        mFABNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewTaskActivity.class);
                startActivity(intent);
            }
        });

        // Set the fragment's toolbar.
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        // Prepare the data for the current date DatePickerDialog
        final DatePickerDialog.OnDateSetListener curDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mCurShownDate = mCalendar.getTime();
                mTasks.removeObserver(mTaskListObserver);
                mTasks = mTaskViewModel.getTasksOfDate(mCurShownDate);
                mTasks.observe(TasksFragment.this, mTaskListObserver);
            }
        };
        mCurDatePickerDialog = new DatePickerDialog(getContext(),
                curDateListener,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
        // Get the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.tasks_frag_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*
        This method handles the toolbar menu selection.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pick_date:
                mCurDatePickerDialog.show();

                return true;

            default:
                // Do nothing.
        }

        return super.onOptionsItemSelected(item);
    }

    private float calculateProgress(List<Task> tasks) {
        float sumComplete = 0;

        // Loop through all the tasks and calculate their sum
        for (int cur = 0; cur < tasks.size(); cur++) {
            if(tasks.get(cur).getIs_done()) {
                sumComplete++;
            }
        }

        return (sumComplete / tasks.size()) * 100;
    }
}
