package com.saurov.attendancemanager.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.LayoutInflaterCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;
import com.orm.SugarRecord;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.adapters.CourseTabAdapter;
import com.saurov.attendancemanager.database.Course;
import com.saurov.attendancemanager.fragments.ClassFragment;
import com.saurov.attendancemanager.fragments.CourseInfoFragment;
import com.saurov.attendancemanager.fragments.CourseStudentFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseDetailActivity extends AppCompatActivity {

    public static final String TAG_COURSE_ID = "TAG_COURSE_ID";

    private CourseTabAdapter mTabAdapter;
    private Course course;

    @BindView(R.id.course_view_pager)
    ViewPager courseViewPager;

    @BindView(R.id.course_tab_layout)
    TabLayout courseTabLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Course Detail");

        actionBar.setElevation(0);

        Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material, getTheme());
        upArrow.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        actionBar.setHomeAsUpIndicator(upArrow);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        long courseId = getIntent().getLongExtra(TAG_COURSE_ID, 0);

        course = SugarRecord.findById(Course.class, courseId);

        initializeTabs(courseId);

    }

    private void initializeTabs(long courseId) {

        mTabAdapter = new CourseTabAdapter(getSupportFragmentManager());

        CourseInfoFragment courseInfoFragment = CourseInfoFragment.newInstance(courseId);

        mTabAdapter.addFragment(courseInfoFragment, "Info");

        CourseStudentFragment studentFragment = CourseStudentFragment.newInstance(courseId);

        mTabAdapter.addFragment(studentFragment, "Students");

        ClassFragment classFragment = ClassFragment.newInstance(courseId);

        mTabAdapter.addFragment(classFragment, "Classes");

        courseViewPager.setAdapter(mTabAdapter);

        courseTabLayout.setupWithViewPager(courseViewPager);

    }
}
