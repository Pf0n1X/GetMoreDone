package com.pf0n1x.getmoredone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pf0n1x.getmoredone.entities.Task;
//import com.pf0n1x.getmoredone.view_models.TaskViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.sql.Time;
import java.util.Date;

public class NewTaskActivity extends AppCompatActivity {

    // Data Members
    private EditText mStartDateEditText;
    private EditText mEndDateEditText;
    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
//    private TaskViewModel mTaskViewModel;

    // Constant Members
    final Calendar mCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        // Initialize views
        mStartDateEditText = findViewById(R.id.edit_text_na_start_date); // TODO: Add setters
        mEndDateEditText = findViewById(R.id.edit_text_na_end_date); // TODO: Add getters
        mTitleEditText = findViewById(R.id.edit_text_na_title);
        mDescriptionEditText = findViewById(R.id.edit_text_na_desc);
//        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        // Set the clickListeners
        final DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(R.id.edit_text_na_start_date);
            }
        };

        final DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(R.id.edit_text_na_end_date);
            }
        };

        mStartDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewTaskActivity.this,
                        startDateListener,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mEndDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewTaskActivity.this,
                        endDateListener,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    /*
        Update the date EditText.
     */
    public void updateLabel(int editTextName) {
        EditText editText = findViewById(editTextName);
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(mCalendar.getTime()));
    }

    public void onSaveTask(View view) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");

        // TODO: Add validation checks for all the fields

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("tasks");
        try {
            DatabaseReference newTaskRef = dbRef.push();
            newTaskRef.setValue(new Task(
                    newTaskRef.getKey(),
                    mTitleEditText.getText().toString(),
                    "1",
                    mDescriptionEditText.getText().toString(),
                    System.currentTimeMillis(),
                    dateFormat.parse(mStartDateEditText.getText().toString()).getTime(),
                    dateFormat.parse(mEndDateEditText.getText().toString()).getTime(),
                    (new Time(System.currentTimeMillis())).toString(), // TODO: Add real time
                    (new Time(System.currentTimeMillis())).toString(), // TODO: Add real time
                    false));
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        try {
//            mTaskViewModel.insert(new Task(mTitleEditText.getText().toString(),
//                    mDescriptionEditText.getText().toString(),
//                    new Date(System.currentTimeMillis()),
//                    dateFormat.parse(mStartDateEditText.getText().toString()),
//                    dateFormat.parse(mEndDateEditText.getText().toString()),
//                    new Time(System.currentTimeMillis()), // TODO: Add real time
//                    new Time(System.currentTimeMillis()), // TODO: Add real time
//            false));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        // Return to the previous activity.
        finish();
    }

    public void onCancelTask(View view) {

        // Return to the previous activity.
        finish();
    }
}
