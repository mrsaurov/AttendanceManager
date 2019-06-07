package com.saurov.attendancemanager.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.saurov.attendancemanager.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttendanceDialogFragment extends DialogFragment {

    @BindView(R.id.cycle_edit_text)
    TextInputEditText cycleEditText;

    @BindView(R.id.cycle_layout)
    TextInputLayout cycleLayout;

    @BindView(R.id.day_spinner)
    Spinner daySpinner;

    @BindView(R.id.period_spinner)
    Spinner periodSpinner;

    private String mDay;
    private String mCycle;
    private String mPeriod;

    public interface onAttendanceDataPassedListener {
        void onDataPassed(String cycle, String day, String period);
    }

    private onAttendanceDataPassedListener listener;

    public void setOnAttendanceDataPassedListener(onAttendanceDataPassedListener listener) {
        this.listener = listener;
    }

    private static final String ARG_COURSE_CLASS_CYCLE = "arg_course_class_cycle";
    private static final String ARG_COURSE_CLASS_DAY = "arg_course_class_day";
    private static final String ARG_COURSE_CLASS_PERIOD = "arg_course_class_period";

    public static AttendanceDialogFragment newInstance(String cycle, String day, String period) {

        Bundle args = new Bundle();
        args.putString(ARG_COURSE_CLASS_CYCLE, cycle);
        args.putString(ARG_COURSE_CLASS_DAY, day);
        args.putString(ARG_COURSE_CLASS_PERIOD, period);
        AttendanceDialogFragment fragment = new AttendanceDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCycle = getArguments().getString(ARG_COURSE_CLASS_CYCLE);
            mDay = getArguments().getString(ARG_COURSE_CLASS_DAY);
            mPeriod = getArguments().getString(ARG_COURSE_CLASS_PERIOD);
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_attendance, null);
        ButterKnife.bind(this, view);

        initializeDaySpinner();
        initializePeriodSpinner();
        //Edit Mode
        if (getArguments() != null) {
            cycleEditText.setText(mCycle);

            switch (mDay) {
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

            switch (mPeriod) {
                case "1":
                    periodSpinner.setSelection(1);
                    break;
                case "2":
                    periodSpinner.setSelection(2);
                    break;
                case "3":
                    periodSpinner.setSelection(3);
                    break;
                case "4":
                    periodSpinner.setSelection(4);
                    break;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view)
                .setTitle("Class Information")
                .setPositiveButton("Continue", null)
                .setNegativeButton("Cancel", null);

        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (isInputValid()) {

                            dismiss();

                            String cycle = cycleEditText.getText().toString();
                            String day = daySpinner.getSelectedItem().toString();

                            if (periodSpinner.getSelectedItemPosition() == 0){
                                periodSpinner.setSelection(1);
                            }

                            String period = periodSpinner.getSelectedItem().toString();

                            if (listener != null) {
                                listener.onDataPassed(cycle, day, period);
                            }
                        }

                    }
                });

                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        //Edit Mode
                        if (getArguments() == null) {
                            getActivity().finish();
                        }

                    }
                });

            }
        });

        return alertDialog;
    }

    private void initializePeriodSpinner() {

        String[] periods = new String[]{
                "Class Period (Default 1)",
                "1",
                "2",
                "3",
                "4",
        };

        final List<String> periodList = new ArrayList<>(Arrays.asList(periods));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getContext(), R.layout.spinner_item, periodList) {

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
        periodSpinner.setAdapter(spinnerArrayAdapter);

        periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                getContext(), R.layout.spinner_item, dayList) {

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

    private boolean isInputValid() {
        boolean isValid = true;

        if (daySpinner.getSelectedItemPosition() == 0) {
            ((TextView) daySpinner.getSelectedView()).setError("Section is required");
            isValid = false;
        }

        if (isTextEmpty(cycleEditText.getText())) {
            cycleLayout.setError("Cycle is required");
            isValid = false;
        } else {
            cycleLayout.setError(null);
        }

        return isValid;

    }

    private boolean isTextEmpty(@Nullable Editable text) {
        return text != null && text.toString().trim().isEmpty();
    }
}
