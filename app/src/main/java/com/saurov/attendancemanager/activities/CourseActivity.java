package com.saurov.attendancemanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;
import com.orm.SugarRecord;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.adapters.CourseAdapter;
import com.saurov.attendancemanager.database.Course;
import com.saurov.attendancemanager.dialogs.CourseBottomSheetDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseActivity extends AppCompatActivity {

    @BindView(R.id.add_course_fab)
    FloatingActionButton fab;

    @BindView(R.id.course_recycler_view)
    RecyclerView courseRecyclerView;

    CourseAdapter adapter;

    public static final String EDIT_COURSE_FLAG = "EDIT_COURSE_FLAG";
    public static final String COURSE_ID_TAG = "COURSE_ID_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        ButterKnife.bind(this);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(CourseActivity.this, AddEditCourseActivity.class);

                startActivity(i);
            }
        });

        List<Course> courseList = Course.listAll(Course.class);

        adapter = new CourseAdapter(this, courseList);
        adapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onClick(Course course, int position) {
                Intent i = new Intent(CourseActivity.this, CourseDetailActivity.class);
                i.putExtra(CourseDetailActivity.TAG_COURSE_ID, course.getId());
                startActivity(i);
            }

            @Override
            public void onMenuClick(Course course, int position) {
                CourseBottomSheetDialogFragment bottomSheet = new CourseBottomSheetDialogFragment();
                bottomSheet.show(CourseActivity.this.getSupportFragmentManager(), "example");

                bottomSheet.setOnItemClickListener(new CourseBottomSheetDialogFragment.BottomSheetListener() {
                    @Override
                    public void onItemClicked(String selectionId) {

                        Intent intent;

                        switch (selectionId) {

                            case CourseBottomSheetDialogFragment.ITEM_OPEN:

                                intent = new Intent(CourseActivity.this, CourseDetailActivity.class);
                                intent.putExtra(CourseDetailActivity.TAG_COURSE_ID, course.getId());

                                startActivity(intent);

                                break;

                            case CourseBottomSheetDialogFragment.ITEM_DELETE:
                                course.delete();

                                refreshCourseRecylerView();
                                break;

                            case CourseBottomSheetDialogFragment.ITEM_EDIT:

                                intent = new Intent(CourseActivity.this, AddEditCourseActivity.class);

                                intent.putExtra(EDIT_COURSE_FLAG, "EDIT");

                                intent.putExtra(COURSE_ID_TAG, course.getId());

                                startActivity(intent);

                                break;
                            case CourseBottomSheetDialogFragment.ITEM_TAKE_ATTENDANCE:

                                intent = new Intent(CourseActivity.this, TakeAttendanceActivity.class);
                                intent.putExtra(TakeAttendanceActivity.TAG_COURSE_ID, course.getId());
                                startActivity(intent);
                                break;
                        }
                    }
                });


            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        courseRecyclerView.setLayoutManager(layoutManager);
        courseRecyclerView.setItemAnimator(new DefaultItemAnimator());
        courseRecyclerView.setAdapter(adapter);
    }

    private void refreshCourseRecylerView() {
        adapter.refreshData(SugarRecord.listAll(Course.class));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshCourseRecylerView();

    }


}
