package com.saurov.attendancemanager.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.orm.SugarRecord;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.database.Course;
import com.saurov.attendancemanager.database.CourseStudent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddStudentActivity extends AppCompatActivity {

    @BindView(R.id.enrollment_first_roll)
    TextInputEditText firstRollEditText;

    @BindView(R.id.enrollment_last_roll)
    TextInputEditText lastRollEditText;

    @BindView(R.id.enrollment_readmitted_rolls)
    TextInputEditText readmittedRollsEditText;

    @BindView(R.id.series_edit_text)
    TextInputEditText excludedRollsEditText;

    @BindView(R.id.save_button)
    MaterialButton saveButton;

    @BindView(R.id.cancel_button)
    MaterialButton cancelButton;

    public static final String TAG_COURSE_ID = "TAG_COURSE_ID";
    SparseBooleanArray isStudentInDb = new SparseBooleanArray();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        ButterKnife.bind(this);

        long courseId = getIntent().getLongExtra(TAG_COURSE_ID, 0);

        Course course = SugarRecord.findById(Course.class,courseId);

        List<CourseStudent> courseStudents = course.getStudents();

        for (CourseStudent student:courseStudents){
            isStudentInDb.put(student.getRoll(), true);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Collecting user input

                int firstRoll = Integer.parseInt(firstRollEditText.getText().toString());
                int lastRoll = Integer.parseInt(lastRollEditText.getText().toString());

                String[] excluded = excludedRollsEditText.getText().toString().split(",");
                String[] readmitted = readmittedRollsEditText.getText().toString().split(",");

                Set<Integer> excludedSet = new HashSet<>(excluded.length);
                Set<Integer> readmittedSet = new HashSet<>(readmitted.length);

                for (String s : excluded) {
                    try {
                        excludedSet.add(Integer.parseInt(s.trim()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                for (String s : readmitted) {
                    try {
                        readmittedSet.add(Integer.parseInt(s.trim()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }


                if (lastRoll > firstRoll) {

                    for (int i = firstRoll; i <= lastRoll; i++) {

                        // Excluded then don't add
                        if (excludedSet.contains(i) || isStudentInDb.get(i)){
                            continue;
                        }

                        CourseStudent student = new CourseStudent();

                        student.setRoll(i);
                        student.setCourse(SugarRecord.findById(Course.class, courseId));

                        student.save();
                    }

                    //Adding readmitted students

                    for (int i : readmittedSet){

                        if (excludedSet.contains(i) || isStudentInDb.get(i)){
                            continue;
                        }

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
