package com.saurov.attendancemanager.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orm.SugarRecord;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.adapters.AttendanceAdapter;
import com.saurov.attendancemanager.database.Course;
import com.saurov.attendancemanager.database.CourseStudent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TakeAttendanceActivity extends AppCompatActivity {

    @BindView(R.id.attendance_recycler_view)
    RecyclerView attendanceRecyclerView;

    AttendanceAdapter adapter;

    public static final String TAG_COURSE_ID = "TAG_COURSE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        ButterKnife.bind(this);

        long courseId = getIntent().getLongExtra(TAG_COURSE_ID, 0);

        Course course = SugarRecord.findById(Course.class, courseId);

        List<CourseStudent> courseStudents = course.getStudents();

        adapter = new AttendanceAdapter(courseStudents);

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);

        attendanceRecyclerView.setLayoutManager(layoutManager);
        attendanceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        attendanceRecyclerView.setAdapter(adapter);

    }
}
