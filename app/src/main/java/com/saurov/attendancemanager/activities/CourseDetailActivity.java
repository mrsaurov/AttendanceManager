package com.saurov.attendancemanager.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.adapters.CourseTabAdapter;
import com.saurov.attendancemanager.fragments.CourseInfoFragment;

public class CourseDetailActivity extends AppCompatActivity {

    private CourseTabAdapter mTabAdapter;

    @BindView(R.id.course_view_pager)
    ViewPager courseViewPager;

    @BindView(R.id.course_tab_layout)
    TabLayout courseTabLayout;

    @BindView(R.id.my_toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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


        mTabAdapter = new CourseTabAdapter(getSupportFragmentManager());

        CourseInfoFragment courseInfoFragment = new CourseInfoFragment();

        mTabAdapter.addFragment(courseInfoFragment, "Info");

        CourseInfoFragment courseInfoFragment2 = new CourseInfoFragment();

        mTabAdapter.addFragment(courseInfoFragment2, "Students");

        courseViewPager.setAdapter(mTabAdapter);

        courseTabLayout.setupWithViewPager(courseViewPager);

    }
}
