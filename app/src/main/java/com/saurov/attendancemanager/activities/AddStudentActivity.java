package com.saurov.attendancemanager.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.orm.SugarRecord;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.database.Course;
import com.saurov.attendancemanager.database.CourseStudent;

public class AddStudentActivity extends AppCompatActivity {

    @BindView(R.id.enrollment_first_roll)
    TextInputEditText firstRollEditText;

    @BindView(R.id.enrollment_last_roll)
    TextInputEditText lastRollEditText;

    @BindView(R.id.enrollment_readmitted_roll)
    TextInputEditText readmittedEditText;

    @BindView(R.id.save_button)
    MaterialButton saveButton;

    @BindView(R.id.cancel_button)
    MaterialButton cancelButton;

    public static final String TAG_COURSE_ID = "TAG_COURSE_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        ButterKnife.bind(this);

        long courseId = getIntent().getLongExtra(TAG_COURSE_ID, 0);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Collecting user input

                int firstRoll = Integer.parseInt(firstRollEditText.getText().toString());
                int lastRoll = Integer.parseInt(lastRollEditText.getText().toString());

                if (lastRoll > firstRoll) {

                    for (int i = firstRoll; i <= lastRoll; i++) {

                        CourseStudent student = new CourseStudent();

                        student.setRoll(i);
                        student.setCourse(SugarRecord.findById(Course.class, courseId));

                        student.save();
                    }

                    finish();

                } else {

                    Toast.makeText(AddStudentActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();

                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelDialog();
            }
        });
    }

    private void showCancelDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure want to cancel?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}
