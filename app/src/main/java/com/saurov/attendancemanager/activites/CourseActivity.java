package com.saurov.attendancemanager.activites;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.View;

import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.adapters.CourseAdapter;
import com.saurov.attendancemanager.database.Course;

import java.util.List;

public class CourseActivity extends AppCompatActivity {

    @BindView(R.id.add_course_fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        ButterKnife.bind(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(CourseActivity.this, AddCourseActivity.class);

                startActivity(i);
            }
        });

        List<Course> courseList = Course.listAll(Course.class);

        RecyclerView courseRecyclerView = findViewById(R.id.course_recycler_view);
        CourseAdapter adapter = new CourseAdapter(this, courseList);

        courseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseRecyclerView.setItemAnimator(new DefaultItemAnimator());
        courseRecyclerView.setAdapter(adapter);
    }
}
