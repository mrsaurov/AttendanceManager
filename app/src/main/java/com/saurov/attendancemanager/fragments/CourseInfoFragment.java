package com.saurov.attendancemanager.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.material.button.MaterialButton;
import com.orm.SugarRecord;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.activities.AddEditAttendanceActivity;
import com.saurov.attendancemanager.activities.AddStudentActivity;
import com.saurov.attendancemanager.database.Course;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CourseInfoFragment extends Fragment {

    @BindView(R.id.class_name_image_view)
    ImageView courseSeriesImageView;

    @BindView(R.id.course_batch_text_view)
    TextView courseClass;

    @BindView(R.id.student_present_text_view)
    TextView courseNameTextView;

    @BindView(R.id.student_absent_text_view)
    TextView courseNoTextView;

    @BindView(R.id.last_class_taken_text_view)
    TextView lastClassTextView;

    @BindView(R.id.total_class_taken_text_view)
    TextView totalClassTakenTextView;

    @BindView(R.id.total_student_text_view)
    TextView totalStudentTextView;

    @BindView(R.id.take_attendance_button)
    MaterialButton takeAttendanceButton;

    private Course course;


    private static final String ARG_COURSE_ID = "arg_course_id";

    private long courseId;

    public CourseInfoFragment() {
        // Required empty public constructor
    }

    public static CourseInfoFragment newInstance(Long courseId) {
        CourseInfoFragment fragment = new CourseInfoFragment();
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

        View view = inflater.inflate(R.layout.fragment_course_info, container, false);

        ButterKnife.bind(this, view);

        courseNameTextView.setText(course.getTitle());
        courseNoTextView.setText(course.getNumber());

        takeAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (course.getTotalStudents() != 0) {

                    Intent i = new Intent(getContext(), AddEditAttendanceActivity.class);
                    i.putExtra(AddEditAttendanceActivity.TAG_COURSE_ID, courseId);
                    startActivity(i);

                } else {
                    new AlertDialog.Builder(getContext())
                            .setCancelable(false)
                            .setTitle("No student enrolled")
                            .setMessage("You have to at least add one student to take attendance")
                            .setPositiveButton("Enroll Student", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent i = new Intent(getContext(), AddStudentActivity.class);
                                    i.putExtra(AddStudentActivity.TAG_COURSE_ID, courseId);

                                    startActivity(i);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create().show();

                }
            }
        });

        ColorGenerator generator = ColorGenerator.MATERIAL;

        int color = generator.getColor(course.getSeries());

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(course.getSeries(), color);

        courseSeriesImageView.setImageDrawable(drawable);

        courseClass.setText(course.getDepartment() + " " + course.getSeries() + " " + course.getSection());

        totalClassTakenTextView.setText(String.valueOf(course.getTotalClassTaken()));
        if (course.getAllClasses().size() != 0) {
            lastClassTextView.setText(course.getLastClassTaken().getHumanReadableDate());
        } else {
            lastClassTextView.setText("N/A");
        }

        totalStudentTextView.setText(String.valueOf(course.getTotalStudents()));

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

}
