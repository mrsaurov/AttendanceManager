package com.saurov.attendancemanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;
import com.saurov.attendancemanager.R;

public class AddStudentActivity extends AppCompatActivity {

    @BindView(R.id.enrollment_first_roll)
    TextInputEditText firstRollEditText;

    @BindView(R.id.enrollment_last_roll)
    TextInputEditText lastRollEditText;

    @BindView(R.id.enrollment_readmitted_roll)
    TextInputEditText readmittedEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        ButterKnife.bind(this);
    }
}
