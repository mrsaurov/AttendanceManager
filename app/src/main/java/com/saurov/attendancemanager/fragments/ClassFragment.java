package com.saurov.attendancemanager.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orm.SugarRecord;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.activities.AddEditAttendanceActivity;
import com.saurov.attendancemanager.adapters.ClassAdapter;
import com.saurov.attendancemanager.database.Course;
import com.saurov.attendancemanager.database.CourseClass;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassFragment extends Fragment {
    public static final String EDIT_CLASS_ATTENDANCE_FLAG = "FLAG_EDIT_CLASS_ATTENDANCE";
    private static final String ARG_COURSE_ID = "param1";

    private long courseId;
    private Course course;
    private ClassAdapter adapter;

    @BindView(R.id.class_recycler_view)
    RecyclerView classRecyclerView;

    public ClassFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param courseId Parameter 1.
     * @return A new instance of fragment ClassFragment.
     */
    public static ClassFragment newInstance(long courseId) {
        ClassFragment fragment = new ClassFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_class, container, false);

        ButterKnife.bind(this, view);

        adapter = new ClassAdapter(getContext(), course.getAllClasses());
        adapter.setOnItemClickListener(new ClassAdapter.onItemClickListener() {
            @Override
            public void onClick(CourseClass courseClass, int position) {
                Intent intent = new Intent(getContext(), AddEditAttendanceActivity.class);
                intent.putExtra(EDIT_CLASS_ATTENDANCE_FLAG, "EDIT");
                intent.putExtra(AddEditAttendanceActivity.TAG_COURSE_ID, courseId);
                intent.putExtra(AddEditAttendanceActivity.TAG_CLASS_ID, courseClass.getId());
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        classRecyclerView.setLayoutManager(layoutManager);
        classRecyclerView.setItemAnimator(new DefaultItemAnimator());
        classRecyclerView.setAdapter(adapter);

        return view;
    }

}
