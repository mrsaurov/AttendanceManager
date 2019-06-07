package com.saurov.attendancemanager.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orm.SugarRecord;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.activities.AddStudentActivity;
import com.saurov.attendancemanager.adapters.StudentAdapter;
import com.saurov.attendancemanager.database.Course;
import com.saurov.attendancemanager.database.CourseStudent;
import com.saurov.attendancemanager.dialogs.StudentBottomSheetDialogFragment;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private StudentAdapter adapter;
    private List<CourseStudent> courseStudentsList;

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
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            courseId = getArguments().getLong(ARG_COURSE_ID);

            course = SugarRecord.findById(Course.class, courseId);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_student, container, false);
        ButterKnife.bind(this, view);

        courseStudentsList = course.getStudents();
        adapter = new StudentAdapter(getContext(), courseStudentsList);
        adapter.setOnItemClickListener(new StudentAdapter.OnItemClickListener() {
            @Override
            public void onClick(CourseStudent student, int position) {

            }

            @Override
            public void onMenuClick(CourseStudent student, int position) {

                StudentBottomSheetDialogFragment bottomSheet = new StudentBottomSheetDialogFragment();
                bottomSheet.show(getFragmentManager(), "student_bottom_sheet");
                bottomSheet.setOnItemClickListener(new StudentBottomSheetDialogFragment.BottomSheetListener() {
                    @Override
                    public void onItemClicked(String selectionId) {
                        switch (selectionId){
                            case StudentBottomSheetDialogFragment.ITEM_DELETE:
                                student.deleteCascade();
                                refreshStudentRecyclerView();
                                break;
                            case StudentBottomSheetDialogFragment.ITEM_OPEN:
                                break;
                        }
                    }
                });

            }
        });

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

    private void refreshStudentRecyclerView() {
        adapter.refreshData(course.getStudents());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshStudentRecyclerView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.course_student_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.search_hint_color));


        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_attendance_asc:
                Collections.sort(courseStudentsList, new Comparator<CourseStudent>() {
                    @Override
                    public int compare(CourseStudent o1, CourseStudent o2) {
                        return Double.compare(o1.getAttendancePercentage(), o2.getAttendancePercentage());
                    }
                });

                break;

            case R.id.menu_item_attendance_desc:
                Collections.sort(courseStudentsList, new Comparator<CourseStudent>() {
                    @Override
                    public int compare(CourseStudent o1, CourseStudent o2) {
                        if (o1.getAttendancePercentage() == o2.getAttendancePercentage()) {
                            return 0;
                        } else if (o1.getAttendancePercentage() > o2.getAttendancePercentage()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });

                break;

            case R.id.menu_item_roll:
                Collections.sort(courseStudentsList, new Comparator<CourseStudent>() {
                    @Override
                    public int compare(CourseStudent o1, CourseStudent o2) {
                        return Integer.compare(o1.getRoll(), o2.getRoll());
                    }
                });

                break;

        }

        adapter.refreshData(courseStudentsList);
        adapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }
}
