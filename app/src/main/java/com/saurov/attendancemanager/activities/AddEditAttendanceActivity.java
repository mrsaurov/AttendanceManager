package com.saurov.attendancemanager.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.orm.SugarRecord;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.adapters.AttendanceAdapter2;
import com.saurov.attendancemanager.database.Attendance;
import com.saurov.attendancemanager.database.Course;
import com.saurov.attendancemanager.database.CourseClass;
import com.saurov.attendancemanager.database.CourseStudent;
import com.saurov.attendancemanager.dialogs.AttendanceDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditAttendanceActivity extends AppCompatActivity {


    @BindView(R.id.attendance_recycler_view)
    RecyclerView attendanceRecyclerView;

    @BindView(R.id.save_button)
    MaterialButton saveButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AttendanceAdapter2 adapter;
    private ActionBar actionBar;

    private CourseClass courseClass;

    public static final String EDIT_CLASS_ATTENDANCE_FLAG = "FLAG_EDIT_CLASS_ATTENDANCE";
    public static final String TAG_COURSE_ID = "TAG_COURSE_ID";
    public static final String TAG_CLASS_ID = "TAG_CLASS_ID";
    private String editFlag;
    private Course course;

    private String mCycle;
    private String mDay;
    private String mPeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        editFlag = getIntent().getStringExtra(EDIT_CLASS_ATTENDANCE_FLAG);

        long courseId = getIntent().getLongExtra(TAG_COURSE_ID, 0);

        course = SugarRecord.findById(Course.class, courseId);

        List<CourseStudent> courseStudents = course.getStudents();

        //New CourseClass
        if (editFlag == null) {
            AttendanceDialogFragment dialogFragment = new AttendanceDialogFragment();
            dialogFragment.setCancelable(false);
            dialogFragment.show(getSupportFragmentManager(), "dialog");
            dialogFragment.setOnAttendanceDataPassedListener(new AttendanceDialogFragment.onAttendanceDataPassedListener() {
                @Override
                public void onDataPassed(String cycle, String day, String period) {
                    mCycle = cycle;
                    mDay = day;
                    mPeriod = period;
                    actionBar.setTitle(cycle + day);
                }
            });

            courseClass = new CourseClass();
            courseClass.setCourse(course);
            adapter = new AttendanceAdapter2(this, courseStudents);
        } else {
            //Edit Request
            long classId = getIntent().getLongExtra(TAG_CLASS_ID, 0);

            courseClass = SugarRecord.findById(CourseClass.class, classId);

            mCycle = courseClass.getCycle();
            mDay = courseClass.getDay();
            mPeriod = String.valueOf(courseClass.getPeriod());

            refreshActionBarTitle();


            adapter = new AttendanceAdapter2(this, courseStudents, courseClass.getAttendedStudents());

        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAttendance(editFlag, course);
            }
        });


        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);

        attendanceRecyclerView.setLayoutManager(layoutManager);
        attendanceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        attendanceRecyclerView.setAdapter(adapter);

    }

    private void saveAttendance(String editFlag, Course course) {
        List<CourseStudent> selectedStudents = adapter.getSelectedStudents();

        courseClass.setCycle(mCycle);
        courseClass.setDay(mDay);
        courseClass.setPeriod(Integer.parseInt(mPeriod));

        if (editFlag == null) {
            //New Attendance
            courseClass.setTimestamp(System.currentTimeMillis());
//            course.setTotalClassTaken(course.getTotalClassTaken() + 1);
            course.save();
        } else {
            //Edit
            //Deleting all old attendance because of new entry for attendances
            courseClass.deleteAllAttendance();
        }

        courseClass.save();

        for (CourseStudent student : selectedStudents) {
            Attendance attendance = new Attendance();

            attendance.setCourseStudent(student);
            attendance.setCourseClass(courseClass);
            attendance.save();

            // Update course info and student attendance percentage
        }

        //Updating student attendance data

        for (CourseStudent student : SugarRecord.listAll(CourseStudent.class)) {
            long totalClassAttended = student.getTotalClassAttended();
            int totalClass = student.getCourse().getTotalClassTaken();
            double attendancePercentage = (totalClassAttended * 100.0) / totalClass;
            int attendanceMark = student.percentageToMark(attendancePercentage);

            student.setAttendancePercentage(attendancePercentage);
            student.setAttendanceMark(attendanceMark);
            student.save();
        }

        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.attendance_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
            case R.id.select_all:

                adapter.selectAllStudents();
//                actionBar.setSubtitle("All item selected");
                break;

            case R.id.deselect_all:
                adapter.clearAllStudents();
                break;

            case R.id.save:
                saveAttendance(editFlag, course);
                break;
            case R.id.edit:
                AttendanceDialogFragment dialogFragment =
                        AttendanceDialogFragment.newInstance(mCycle, mDay, mPeriod);
                dialogFragment.setCancelable(false);
                dialogFragment.show(getSupportFragmentManager(), "dialog_edit");
                dialogFragment.setOnAttendanceDataPassedListener(new AttendanceDialogFragment.onAttendanceDataPassedListener() {
                    @Override
                    public void onDataPassed(String cycle, String day, String period) {
                        mCycle = cycle;
                        mDay = day;
                        mPeriod = period;
                        refreshActionBarTitle();
                    }
                });
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshActionBarTitle(){
        actionBar.setTitle(mCycle + mDay);
    }
}
