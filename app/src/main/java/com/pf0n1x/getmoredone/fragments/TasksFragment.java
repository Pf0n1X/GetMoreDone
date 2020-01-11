package com.pf0n1x.getmoredone.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pf0n1x.getmoredone.NewTaskActivity;
import com.pf0n1x.getmoredone.R;
import com.pf0n1x.getmoredone.adapters.TaskListAdapter;
import com.pf0n1x.getmoredone.entities.Account;
import com.pf0n1x.getmoredone.entities.Task;
import com.skydoves.progressview.ProgressView;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TasksFragment extends Fragment {

    // Data Members
    private ProgressView mProgressView;
    private FloatingActionButton mFABNewTask;
    private Date mCurShownDate;
    private DatePickerDialog mCurDatePickerDialog;
    private DatabaseReference mDatesRef;
    private List<Task> mTaskList;
    private TaskListAdapter mAdapter;
    private ChildEventListener mTaskEventListener;
    private TextView mStreakTextView;
    private TextView mMoneyTextView;
    private ImageView mStreakImageView;
    private Account mCurUser;

    // Constant Members
    private final Calendar mCalendar = Calendar.getInstance();
    private final FirebaseDatabase mDb = FirebaseDatabase.getInstance();
    private final DatabaseReference mUserDBRef = mDb
            .getReference("users").child(FirebaseAuth.getInstance()
                    .getCurrentUser().getUid());

    // TODO: Split this methhod to many sub methods
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the fragment.
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        // Save the progress view.
        mProgressView = view.findViewById(R.id.progressViewTasks); // TODO: Define a setter
        mFABNewTask = view.findViewById(R.id.fab_new_task); // TODO: Define a setter
        mStreakTextView = view.findViewById(R.id.textview_streak);
        mMoneyTextView = view.findViewById(R.id.textview_money);
        mStreakImageView = view.findViewById(R.id.imageview_streak);
        mTaskList = new LinkedList<Task>();
        mAdapter = new TaskListAdapter(this.getContext());

        RecyclerView recyclerTasksView = view.findViewById(R.id.recyclerview_tasks);

        recyclerTasksView.setAdapter(mAdapter);
        LinearLayoutManager listLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerTasksView.setLayoutManager(listLayoutManager);
        recyclerTasksView.addItemDecoration(new DividerItemDecoration(this.getContext(),
                listLayoutManager.getOrientation()));
        mCurShownDate = new Date(System.currentTimeMillis());

        // Save the event listener's behavior.
        mTaskEventListener = new ChildEventListener() { // TODO: Move this to a single object
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Task newTask = dataSnapshot.getValue(Task.class);
                if (newTask.getEnd_date() >= mCurShownDate.getTime()) {
                    mTaskList.add(newTask);
                }

                // Get the current progress
                int progress = mAdapter.getCompletedTasksCount();

                // Set the percentage according to the
                // user's progress
                mProgressView.setMax(mTaskList.size());
                mProgressView.setProgress(progress);
                mProgressView.setLabelText(progress + "/" + mTaskList.size());
                Collections.sort(mTaskList);
                mAdapter.setTasks(mTaskList);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                // Get the current progress
                int progress = mAdapter.getCompletedTasksCount();

                // Set the percentage according to the
                // user's progress
                mProgressView.setMax(mTaskList.size());
                mProgressView.setProgress(progress);
                Collections.sort(mTaskList);
                mAdapter.setTasks(mTaskList);
                mProgressView.setLabelText(progress + "/" + mTaskList.size());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatesRef = FirebaseDatabase
                .getInstance()
                .getReference("user_collections/"
                        + FirebaseAuth.getInstance().getCurrentUser().getUid()
                        + "/tasks");
        mDatesRef.orderByChild("start_date")
                .endAt(mCurShownDate.getTime())
                .addChildEventListener(mTaskEventListener);

        // Set the onClick event for the new task activity.
        mFABNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TasksFragment.this.getContext(), NewTaskActivity.class);
                startActivity(intent);
            }
        });

        // Set the fragment's toolbar.
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        // Prepare the data for the current date DatePickerDialog
        final DatePickerDialog.OnDateSetListener curDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mCurShownDate = mCalendar.getTime();
                mTaskList.clear();
                mDatesRef.orderByChild("start_date")
                        .endAt(mCurShownDate.getTime())
                        .addChildEventListener(mTaskEventListener);
            }
        };
        mCurDatePickerDialog = new DatePickerDialog(getContext(),
                curDateListener,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));

        // Set the user account update behaviour
        setUserUpdateBehaviour();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

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

    private void setUserUpdateBehaviour() {
        ValueEventListener accountEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mCurUser = dataSnapshot.getValue(Account.class);
                mStreakTextView.setText(mCurUser.getStreak() + "");
                mMoneyTextView.setText(mCurUser.getMoney() + "");
                mAdapter.setCurUser(mCurUser);
                handleStreakColoring(mCurUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mUserDBRef.keepSynced(true);
        mUserDBRef.addValueEventListener(accountEventListener);
    }

    private void handleStreakColoring(Account account) {
        Date today = new Date();
        float daysDiff = (new Date().getTime() - account.getLastActiveDate()) / (1000 * 3600 * 24);

        // If the last active date is today, make the TextView and Image appear colored.
        // Otherwise, grey them out.
        if (daysDiff == 0) {
            mStreakTextView.setTextColor(Color.BLACK);
            mStreakImageView.setImageResource(R.drawable.icons8_lightning_bolt_100);
        } else {
            mStreakTextView.setTextColor(Color.GRAY);
            mStreakImageView.setImageResource(R.drawable.icons8_lightning_bolt_100_disabled);
        }
    }
}
