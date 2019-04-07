package com.saurov.attendancemanager.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.adapters.CourseAdapter;
import com.saurov.attendancemanager.database.Course;
import com.saurov.attendancemanager.dialogs.CourseBottomSheetDialogFragment;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseActivity extends AppCompatActivity {

    @BindView(R.id.add_course_fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
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
        adapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onClick(Course course, int position) {
                Toast.makeText(CourseActivity.this, course.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMenuClick(Course course, int position) {
                CourseBottomSheetDialogFragment bottomSheet = new CourseBottomSheetDialogFragment();
                bottomSheet.show(CourseActivity.this.getSupportFragmentManager(), "example");

                bottomSheet.setOnItemClickListener(new CourseBottomSheetDialogFragment.BottomSheetListener() {
                    @Override
                    public void onItemClicked(String selectionId) {

                        switch (selectionId) {

                            case CourseBottomSheetDialogFragment.ITEM_OPEN:
                                Toast.makeText(CourseActivity.this, "Open", Toast.LENGTH_SHORT).show();
                                break;

                            case CourseBottomSheetDialogFragment.ITEM_DELETE:
                                course.delete();
                                break;

                            case CourseBottomSheetDialogFragment.ITEM_EDIT:
                                Toast.makeText(CourseActivity.this, "Edit", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });


            }
        });

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        courseRecyclerView.setLayoutManager(layoutManager);
        courseRecyclerView.setItemAnimator(new DefaultItemAnimator());
        courseRecyclerView.setAdapter(adapter);
    }
}
