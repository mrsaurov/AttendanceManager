package com.saurov.attendancemanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.orm.SugarRecord;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.database.Course;
import com.saurov.attendancemanager.database.CourseClass;
import com.saurov.attendancemanager.database.CourseStudent;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassDetailActivity extends AppCompatActivity {

    public static final String TAG_CLASS_ID = "TAG_CLASS_ID";
    public static final String TAG_COURSE_ID = "TAG_COURSE_ID";

    @BindView(R.id.class_name_image_view)
    ImageView classNameImageView;

    @BindView(R.id.class_date_text_view)
    TextView classDateTextView;

    @BindView(R.id.student_present_text_view)
    TextView studentPresentTextView;

    @BindView(R.id.student_absent_text_view)
    TextView studentAbsentTextView;

    @BindView(R.id.student_present_chip_group)
    ChipGroup studentPresentChipGroup;

    @BindView(R.id.student_absent_chip_group)
    ChipGroup studentAbsentChipGroup;

    SparseBooleanArray attendedStudent;

    private long classId;
    private long courseId;
    private CourseClass courseClass;
    private Course course;
    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);

        ButterKnife.bind(this);

        classId = getIntent().getLongExtra(TAG_CLASS_ID, 0);
        courseId = getIntent().getLongExtra(TAG_COURSE_ID, 0);

    }

    private void createView() {

        attendedStudent = new SparseBooleanArray();

        courseClass = SugarRecord.findById(CourseClass.class, classId);
        course = SugarRecord.findById(Course.class, courseId);

        className = courseClass.getCycle() + courseClass.getDay();

        ColorGenerator generator = ColorGenerator.MATERIAL;

        int color = generator.getColor(courseClass.getCycle());
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(className, color);

        classNameImageView.setImageDrawable(drawable);

        classDateTextView.setText(courseClass.getHumanReadableDate());

        studentPresentTextView.setText(String.valueOf(courseClass.getTotalStudentPresent()));
        studentAbsentTextView.setText(String.valueOf(
                course.getTotalStudents() - courseClass.getTotalStudentPresent()));

        studentPresentChipGroup.removeAllViews();
        for (CourseStudent student : courseClass.getAttendedStudents()) {
            attendedStudent.put(student.getRoll(), true);
            Chip chip = new Chip(this);
            chip.setText(String.valueOf(student.getRoll()));
            studentPresentChipGroup.addView(chip);
        }

        studentAbsentChipGroup.removeAllViews();
        for (CourseStudent student : course.getStudents()) {

            if (!attendedStudent.get(student.getRoll())) {
                Chip chip = new Chip(this);
                chip.setText(String.valueOf(student.getRoll()));
                studentAbsentChipGroup.addView(chip);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        createView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.class_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(this, AddEditAttendanceActivity.class);
                intent.putExtra(AddEditAttendanceActivity.EDIT_CLASS_ATTENDANCE_FLAG, "EDIT");
                intent.putExtra(AddEditAttendanceActivity.TAG_COURSE_ID, courseId);
                intent.putExtra(AddEditAttendanceActivity.TAG_CLASS_ID, classId);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
