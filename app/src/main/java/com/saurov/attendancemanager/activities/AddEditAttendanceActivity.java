package com.saurov.attendancemanager.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.saurov.attendancemanager.fragments.ClassFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditAttendanceActivity extends AppCompatActivity {


    @BindView(R.id.attendance_recycler_view)
    RecyclerView attendanceRecyclerView;

    @BindView(R.id.day_spinner)
    Spinner daySpinner;

    @BindView(R.id.cycle_edit_text)
    EditText cycleEditText;

    @BindView(R.id.save_button)
    MaterialButton saveButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    AttendanceAdapter2 adapter;
    //    AttendanceAdapter adapter;
    ActionBar actionBar;

    CourseClass courseClass;

    public static final String EDIT_CLASS_ATTENDANCE_FLAG = "FLAG_EDIT_CLASS_ATTENDANCE";
    public static final String TAG_COURSE_ID = "TAG_COURSE_ID";
    public static final String TAG_CLASS_ID = "TAG_CLASS_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        ButterKnife.bind(this);

        initializeDaySpinner();

        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        actionBar.setTitle("Attendance");

        long courseId = getIntent().getLongExtra(TAG_COURSE_ID, 0);

        Course course = SugarRecord.findById(Course.class, courseId);

        List<CourseStudent> courseStudents = course.getStudents();

        String editFlag = getIntent().getStringExtra(EDIT_CLASS_ATTENDANCE_FLAG);

        if (editFlag != null) {
            //Edit Request

            long classId = getIntent().getLongExtra(TAG_CLASS_ID, 0);

            courseClass = SugarRecord.findById(CourseClass.class, classId);

            cycleEditText.setText(courseClass.getCycle());

            switch (courseClass.getDay()) {
                case "A":
                    daySpinner.setSelection(1);
                    break;
                case "B":
                    daySpinner.setSelection(2);
                    break;
                case "C":
                    daySpinner.setSelection(3);
                    break;
                case "D":
                    daySpinner.setSelection(4);
                    break;
                case "E":
                    daySpinner.setSelection(5);
                    break;
            }

            adapter = new AttendanceAdapter2(this, courseStudents, courseClass.getAttendedStudents());

        } else {
            courseClass = new CourseClass();
            adapter = new AttendanceAdapter2(this, courseStudents);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<CourseStudent> selectedStudents = adapter.getSelectedStudents();

                String cycle = cycleEditText.getText().toString();

                String day = daySpinner.getSelectedItem().toString();

                courseClass.setCycle(cycle);
                courseClass.setDay(day);

                if (editFlag == null) {
                    //New Attendance
                    courseClass.setTimestamp(System.currentTimeMillis());
                    course.setTotalClassTaken(course.getTotalClassTaken() + 1);
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
        });


        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);

        attendanceRecyclerView.setLayoutManager(layoutManager);
        attendanceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        attendanceRecyclerView.setAdapter(adapter);

    }

    private void initializeDaySpinner() {

        String[] days = new String[]{
                "Day",
                "A",
                "B",
                "C",
                "D",
                "E"
        };

        final List<String> dayList = new ArrayList<>(Arrays.asList(days));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, dayList) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setPadding(0, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
                return view;
            }

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);

                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };


        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        daySpinner.setAdapter(spinnerArrayAdapter);

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);

                if (position != 0) {

                    TextView selectedText = view.findViewById(R.id.spinner_text_view);
                    selectedText.setTextColor(getResources().getColor(R.color.spinnerSelectedTextColor));
                }
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
//                    Toast.makeText
//                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
//                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

            case R.id.select_all:

                adapter.selectAllStudents();
//                actionBar.setSubtitle("All item selected");
                break;

            case R.id.deselect_all:
                adapter.clearAllStudents();
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
