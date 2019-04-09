package com.saurov.attendancemanager.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.activities.AddStudentActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseStudentFragment extends Fragment {

    @BindView(R.id.add_student_fab)
    FloatingActionButton fab;

    @BindView(R.id.student_recycler_view)
    RecyclerView studentRecyclerView;


    public CourseStudentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_student, container, false);
        ButterKnife.bind(this, view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), AddStudentActivity.class);

                startActivity(i);

            }
        });

        return view;
    }

}
