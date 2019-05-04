package com.saurov.attendancemanager.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orm.SugarRecord;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.activities.AddStudentActivity;
import com.saurov.attendancemanager.adapters.StudentAdapter;
import com.saurov.attendancemanager.database.Course;
import com.saurov.attendancemanager.database.CourseStudent;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseStudentFragment extends Fragment {

    @BindView(R.id.add_student_fab)
    FloatingActionButton fab;

    @BindView(R.id.student_recycler_view)
    RecyclerView studentRecyclerView;

    private static final String ARG_COURSE_ID = "arg_course_id";

    private long courseId;
    private Course course;

    public CourseStudentFragment() {
        // Required empty public constructor
    }

    public static CourseStudentFragment newInstance(Long courseId) {
        CourseStudentFragment fragment = new CourseStudentFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_COURSE_ID, courseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseId = getArguments().getLong(ARG_COURSE_ID);

            course = SugarRecord.findById(Course.class, courseId);

            List<CourseStudent> courseStudents = course.getStudents();

            Log.d("Saurov", courseStudents.toString());

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_student, container, false);
        ButterKnife.bind(this, view);

        StudentAdapter adapter = new StudentAdapter(getContext(), course.getStudents());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        studentRecyclerView.setLayoutManager(layoutManager);
        studentRecyclerView.setItemAnimator(new DefaultItemAnimator());
        studentRecyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), AddStudentActivity.class);
                i.putExtra(AddStudentActivity.TAG_COURSE_ID, courseId);

                startActivity(i);


            }
        });

        return view;
    }


}
