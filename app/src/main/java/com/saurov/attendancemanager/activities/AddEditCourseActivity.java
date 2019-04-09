package com.saurov.attendancemanager.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.orm.SugarRecord;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.database.Course;

public class AddEditCourseActivity extends AppCompatActivity {

    @BindView(R.id.course_name_edit_text)
    TextInputEditText courseName;

    @BindView(R.id.department_name_edit_text)
    TextInputEditText departmentName;

    @BindView(R.id.course_no_edit_text)
    TextInputEditText courseNo;

    @BindView(R.id.series_edit_text)
    TextInputEditText series;

    @BindView(R.id.section_radio_group)
    RadioGroup sectionRadioGroup;

    @BindView(R.id.save_button)
    MaterialButton saveButton;

    @BindView(R.id.cancel_button)
    MaterialButton cancelButton;

    Course course;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        ButterKnife.bind(this);

        String editflag = getIntent().getStringExtra(CourseActivity.EDIT_COURSE_FLAG);


        if (editflag != null) {

            //Edit Request

            long courseId = getIntent().getLongExtra(CourseActivity.COURSE_ID_TAG, 0);

            course = Course.findById(Course.class, courseId);

            courseName.setText(course.getTitle());
            series.setText(course.getSeries());
            courseNo.setText(course.getNumber());
            departmentName.setText(course.getDepartment());

            switch (course.getSection()) {
                case "A":
                    sectionRadioGroup.check(R.id.a_radio_button);
                    break;

                case "B":
                    sectionRadioGroup.check(R.id.b_radio_button);
                    break;

                case "C":
                    sectionRadioGroup.check(R.id.c_radio_button);
                    break;
            }


            saveButton.setText("Edit");

            course.save();

        } else {

            //Save Request

            course = new Course();

        }


        //Saving or editing course
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (sectionRadioGroup.getCheckedRadioButtonId()) {

                    case R.id.a_radio_button:
                        course.setSection("A");
                        break;
                    case R.id.b_radio_button:
                        course.setSection("B");
                        break;
                    case R.id.c_radio_button:
                        course.setSection("C");
                        break;
                }

                course.setTitle(courseName.getText().toString());
                course.setDepartment(departmentName.getText().toString());
                course.setNumber(courseNo.getText().toString());
                course.setSeries(series.getText().toString());

                course.save();
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCancelDialog();
            }
        });


    }

    @Override
    public void onBackPressed() {
//        showCancelDialog();
        finish();
    }

    private void showCancelDialog() {
        new AlertDialog.Builder(AddEditCourseActivity.this)
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

