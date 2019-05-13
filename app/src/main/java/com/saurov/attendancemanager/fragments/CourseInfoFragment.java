package com.saurov.attendancemanager.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.material.button.MaterialButton;
import com.orm.SugarRecord;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.activities.TakeAttendanceActivity;
import com.saurov.attendancemanager.database.Attendance;
import com.saurov.attendancemanager.database.Course;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseInfoFragment extends Fragment {

    @BindView(R.id.course_series_image_view)
    ImageView courseSeriesImageView;

    @BindView(R.id.course_class)
    TextView courseClass;

    @BindView(R.id.course_name_text_view)
    TextView courseNameTextView;

    @BindView(R.id.course_no_text_view)
    TextView courseNoTextView;

    @BindView(R.id.take_attendance_button)
    MaterialButton takeAttendanceButton;

    private Course course;


    private static final String ARG_COURSE_ID = "arg_course_id";

    private long courseId;
    private OnFragmentInteractionListener mListener;

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


//        for (Attendance a : course.getAllClasses()) {
//            Log.d("Attendance", a.getCycle() + " " + a.getDay());
//        }

        courseNameTextView.setText(course.getTitle());
        courseNoTextView.setText(course.getNumber());

        course.getAllClasses();

        takeAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), TakeAttendanceActivity.class);
                i.putExtra(TakeAttendanceActivity.TAG_COURSE_ID, courseId);
                startActivity(i);
            }
        });

        ColorGenerator generator = ColorGenerator.MATERIAL;

        int color = generator.getColor(course.getSeries());

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(course.getSeries(), color);

        courseSeriesImageView.setImageDrawable(drawable);

        courseClass.setText(course.getDepartment() + " " + course.getSeries() + " " + course.getSection());


        return view;
    }


//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
