package com.saurov.attendancemanager.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.orm.SugarRecord;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.database.Course;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditCourseActivity extends AppCompatActivity{

    @BindView(R.id.course_name_edit_text)
    TextInputEditText courseName;

    @BindView(R.id.department_name_edit_text)
    TextInputEditText departmentName;

    @BindView(R.id.course_no_edit_text)
    TextInputEditText courseNo;

    @BindView(R.id.series_edit_text)
    TextInputEditText series;

    @BindView(R.id.section_spinner)
    Spinner sectionSpinner;

    @BindView(R.id.save_button)
    MaterialButton saveButton;

    @BindView(R.id.cancel_button)
    MaterialButton cancelButton;

    Course course;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        ButterKnife.bind(this);

        initializeSectionSpinner();

        //////////

        String editFlag = getIntent().getStringExtra(CourseActivity.EDIT_COURSE_FLAG);

        if (editFlag != null) {

            //Edit Request

            long courseId = getIntent().getLongExtra(CourseActivity.COURSE_ID_TAG, 0);

            course = SugarRecord.findById(Course.class, courseId);

            courseName.setText(course.getTitle());
            series.setText(course.getSeries());
            courseNo.setText(course.getNumber());
            departmentName.setText(course.getDepartment());

            switch (course.getSection()) {
                case "A":
                    sectionSpinner.setSelection(1);
                    break;

                case "B":
                    sectionSpinner.setSelection(2);
                    break;

                case "C":
                    sectionSpinner.setSelection(3);
                    break;
            }


            saveButton.setText("Edit");

            course.save();

        } else {

            //Save Request

            course = new Course();

        }


        //Saving or editing course
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (sectionSpinner.getSelectedItemPosition()){

                    case 1:
                        course.setSection("A");
                        break;
                    case 2:
                        course.setSection("B");
                        break;
                    case 3:
                        course.setSection("C");
                        break;

                }


                course.setTitle(courseName.getText().toString());
                course.setDepartment(departmentName.getText().toString());
                course.setNumber(courseNo.getText().toString());
                course.setSeries(series.getText().toString());

                course.save();
//                if (onCourseSavedListener != null){
//                    onCourseSavedListener.onCourseSaved();
//                }
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCancelDialog();
            }
        });


    }

    @Override
    public void onBackPressed() {
//        showCancelDialog();
        finish();
    }

    private void initializeSectionSpinner() {

        String[] section = new String[]{
                "Section",
                "A Section",
                "B Section",
                "C Section"
        };

        final List<String> sectionList = new ArrayList<>(Arrays.asList(section));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, sectionList) {

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
        sectionSpinner.setAdapter(spinnerArrayAdapter);

        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void showCancelDialog() {
        new AlertDialog.Builder(AddEditCourseActivity.this)
                .setMessage("Are you sure want to cancel?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

//    public interface OnCourseSavedListener{
//        void onCourseSaved();
//    }
//
//    OnCourseSavedListener onCourseSavedListener;
//
//    public void setOnCourseSavedListener(OnCourseSavedListener listener){
//        this.onCourseSavedListener = listener;
//    }
}

