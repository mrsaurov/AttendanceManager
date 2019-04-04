package com.saurov.attendancemanager.activites;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.adapters.CourseAdapter;
import com.saurov.attendancemanager.database.Course;

import java.util.List;

public class CourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Course course = new Course();

                course.setNumber("CSE 3203");
                course.setTitle("Computer Architecture & Design");
                course.setSeries("15");
                course.setDepartment("CSE");
                course.setSection("B");
                course.save();

                Snackbar.make(view, "Course Added", Snackbar.LENGTH_LONG).show();
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
