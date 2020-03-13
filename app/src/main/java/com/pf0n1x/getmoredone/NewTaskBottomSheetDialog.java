package com.pf0n1x.getmoredone;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pf0n1x.getmoredone.entities.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewTaskBottomSheetDialog extends BottomSheetDialogFragment {

    // Data Members
    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private Button mSaveButton;
    private Button mDateButton;
    private String mTaskDate;
    private View mCurView;

    // Constant Members
    final Calendar mCalendar = Calendar.getInstance();
    private final SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);

        View view = inflater.inflate(R.layout.activity_new_task, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCurView = view;

        // Initialize views
        mTitleEditText = view.findViewById(R.id.edit_text_na_title);
        mDescriptionEditText = view.findViewById(R.id.edit_text_na_desc);
        mSaveButton = view.findViewById(R.id.save_na_button);
        mDateButton = view.findViewById(R.id.button_date);

        // Initialize date with today's day.
        mTaskDate = dtFormat.format(mCalendar.getTime());

        // Set the clickListeners
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDateButton(v);
            }
        });

        // Get the title focused and pull out the keyboard.
        mTitleEditText.requestFocus();
        InputMethodManager imm
                = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
    }

    public void saveTask() {

        // TODO: Add validation checks for all the fields

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef =
                database.getReference
                        ("user_collections/"
                                + FirebaseAuth.getInstance().getCurrentUser().getUid()
                                + "/tasks");

        DatabaseReference newTaskRef = dbRef.push();
        newTaskRef.setValue(new Task(
                newTaskRef.getKey(),
                mTitleEditText.getText().toString(),
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                mDescriptionEditText.getText().toString(),
                System.currentTimeMillis(),
                mTaskDate,
                false));

        // Return to the previous activity.
        dismiss();
    }

    public void onClickDateButton(View view) {
        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(year, month, dayOfMonth, 0, 0, 0);
                mTaskDate = dtFormat.format(mCalendar.getTime());
            }
        };
        new DatePickerDialog(getContext(),
                dateListener,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
