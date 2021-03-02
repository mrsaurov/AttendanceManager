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
import com.saurov.attendancemanager.activities.ClassDetailActivity;
import com.saurov.attendancemanager.adapters.ClassAdapter;
import com.saurov.attendancemanager.database.Course;
import com.saurov.attendancemanager.database.CourseClass;
import com.saurov.attendancemanager.database.CourseStudent;
import com.saurov.attendancemanager.dialogs.ClassBottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassFragment extends Fragment {
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

                Intent intent = new Intent(getContext(), ClassDetailActivity.class);
                intent.putExtra(ClassDetailActivity.TAG_CLASS_ID, courseClass.getId());
                intent.putExtra(ClassDetailActivity.TAG_COURSE_ID, course.getId());
                startActivity(intent);
            }

            @Override
            public void onMenuClick(CourseClass courseClass, int position) {

                ClassBottomSheetDialogFragment bottomSheet = new ClassBottomSheetDialogFragment();
                bottomSheet.show(getFragmentManager(), "example");

                bottomSheet.setOnItemClickListener(new ClassBottomSheetDialogFragment.BottomSheetListener() {
                    @Override
                    public void onItemClicked(String selectionId) {

                        Intent intent;

                        switch (selectionId) {
                            case ClassBottomSheetDialogFragment.ITEM_OPEN:
                                intent = new Intent(getContext(), ClassDetailActivity.class);
                                intent.putExtra(ClassDetailActivity.TAG_CLASS_ID, courseClass.getId());
                                intent.putExtra(ClassDetailActivity.TAG_COURSE_ID, course.getId());
                                startActivity(intent);

                                break;

                            case ClassBottomSheetDialogFragment.ITEM_DELETE:
                                courseClass.deleteCascade();
//                                course.setTotalClassTaken(course.getTotalClassTaken() - 1);
                                course.save();

                                for (CourseStudent student : SugarRecord.listAll(CourseStudent.class)) {
                                    long totalClassAttended = student.getTotalClassAttended();
                                    int totalClass = student.getCourse().getTotalClassTaken();
                                    double attendancePercentage = (totalClassAttended * 100.0) / totalClass;
                                    int attendanceMark = student.percentageToMark(attendancePercentage);

                                    student.setAttendancePercentage(attendancePercentage);
                                    student.setAttendanceMark(attendanceMark);
                                    student.save();
                                }

                                refreshClassRecyclerView();
                                break;

                            case ClassBottomSheetDialogFragment.ITEM_EDIT:

                                intent = new Intent(getContext(), AddEditAttendanceActivity.class);
                                intent.putExtra(AddEditAttendanceActivity.EDIT_CLASS_ATTENDANCE_FLAG, "EDIT");
                                intent.putExtra(AddEditAttendanceActivity.TAG_COURSE_ID, courseId);
                                intent.putExtra(AddEditAttendanceActivity.TAG_CLASS_ID, courseClass.getId());
                                startActivity(intent);
                                break;
                        }
                    }
                });


            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        classRecyclerView.setLayoutManager(layoutManager);
        classRecyclerView.setItemAnimator(new DefaultItemAnimator());
        classRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshClassRecyclerView();
    }

    private void refreshClassRecyclerView() {
        adapter.refreshData(course.getAllClasses());
        adapter.notifyDataSetChanged();
    }
}
