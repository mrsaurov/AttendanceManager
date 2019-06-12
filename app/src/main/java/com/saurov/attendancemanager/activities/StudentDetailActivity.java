package com.saurov.attendancemanager.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.orm.SugarRecord;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.database.CourseClass;
import com.saurov.attendancemanager.database.CourseStudent;
import com.vaibhavlakhera.circularprogressview.CircularProgressView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudentDetailActivity extends AppCompatActivity {

    public static final String TAG_STUDENT_ID = "TAG_STUDENT_ID";
    long studentId;
    CourseStudent student;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.attendance_percentage_circular_progress)
    CircularProgressView attendancePercentageCircularProgress;

    @BindView(R.id.student_absent_chip_group)
    ChipGroup studentAbsentChipGroup;

    @BindView(R.id.student_present_chip_group)
    ChipGroup studentPresentChipGroup;

    @BindView(R.id.student_present_text_view)
    TextView studentPresentTextView;

    @BindView(R.id.student_absent_text_view)
    TextView studentAbsentTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
        ButterKnife.bind(this);

        studentId = getIntent().getLongExtra(TAG_STUDENT_ID, 0);
        student = SugarRecord.findById(CourseStudent.class, studentId);


        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(String.valueOf(student.getRoll()));
        Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material, getTheme());
        upArrow.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        actionBar.setHomeAsUpIndicator(upArrow);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        double attendancePercentage = student.getAttendancePercentage();

        attendancePercentageCircularProgress.setProgress((int) Math.round(attendancePercentage), false);
        attendancePercentageCircularProgress
                .setProgressColor(getAttendancePercentageProgressColor(attendancePercentage));

        List<CourseClass> classesAttended = student.getClassesAttended();
        List<CourseClass> classesAbsent = student.getClassesAbsent();

        studentPresentTextView.setText(String.valueOf(classesAttended.size()));
        studentAbsentTextView.setText(String.valueOf(classesAbsent.size()));

        studentPresentChipGroup.removeAllViews();
        for (CourseClass courseClass : classesAttended) {
            Chip chip = new Chip(this);
            chip.setText(courseClass.getCycle() + courseClass.getDay());
            studentPresentChipGroup.addView(chip);
        }

        studentAbsentChipGroup.removeAllViews();

        for (CourseClass courseClass : classesAbsent) {
                Chip chip = new Chip(this);
                chip.setText(courseClass.getCycle() + courseClass.getDay());
                studentAbsentChipGroup.addView(chip);
        }


    }

    private int getAttendancePercentageProgressColor(double attendancePercentage) {

        if (attendancePercentage == 100) {
            return ContextCompat.getColor(this, R.color.progress_excellent);
        } else if (attendancePercentage >= 70) {
            return ContextCompat.getColor(this, R.color.progress_good);
        } else if (attendancePercentage >= 50) {
            return ContextCompat.getColor(this, R.color.progress_average);
        } else {
            return ContextCompat.getColor(this, R.color.progress_bad);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
