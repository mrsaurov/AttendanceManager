package com.saurov.attendancemanager.activites;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.QuickContactBadge;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.database.Course;

public class AddCourseActivity extends AppCompatActivity {

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

    @BindView(R.id.next_button)
    MaterialButton nextButton;

    @BindView(R.id.cancel_button)
    MaterialButton cancelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        ButterKnife.bind(this);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Course course = new Course();

                switch (sectionRadioGroup.getCheckedRadioButtonId()) {

                    case R.id.a_radio_button:
                        Toast.makeText(AddCourseActivity.this, "A", Toast.LENGTH_SHORT).show();
                        course.setSection("A");
                        break;
                    case R.id.b_radio_button:
                        Toast.makeText(AddCourseActivity.this, "B", Toast.LENGTH_SHORT).show();
                        course.setSection("B");
                        break;
                    case R.id.c_radio_button:
                        Toast.makeText(AddCourseActivity.this, "C", Toast.LENGTH_SHORT).show();
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


    }
}

