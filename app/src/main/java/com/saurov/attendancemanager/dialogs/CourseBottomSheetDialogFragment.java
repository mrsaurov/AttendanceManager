package com.saurov.attendancemanager.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.saurov.attendancemanager.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CourseBottomSheetDialogFragment extends RoundedBottomSheetDialogFragment {


    public interface BottomSheetListener {
        void onItemClicked(String selectionId);
    }

    private BottomSheetListener mListener;


    public static final String ITEM_EDIT = "CourseBottomSheetDialogFragment.EDIT";
    public static final String ITEM_DELETE = "CourseBottomSheetDialogFragment.DELETE";
    public static final String ITEM_OPEN = "CourseBottomSheetDialogFragment.OPEN";
    public static final String ITEM_TAKE_ATTENDANCE = "CourseBottomSheetDialogFragment.TAKE_ATTENDANCE";
    public static final String ITEM_EXPORT_PDF = "CourseBottomSheetDialogFragment.EXPORT_PDF";


    public void setOnItemClickListener(BottomSheetListener listener) {
        this.mListener = listener;
    }

    //Click on Edit Button
    @OnClick(R.id.editItem)
    public void edit(View view) {

        if (mListener != null) {
            mListener.onItemClicked(ITEM_EDIT);
        }

        dismiss();
    }

    //Click on Delete Button
    @OnClick(R.id.deleteItem)
    public void delete(View view) {
        if (mListener != null) {
            mListener.onItemClicked(ITEM_DELETE);
        }

        dismiss();
    }

    //Click on Open Button
    @OnClick(R.id.openItem)
    public void open(View view) {
        if (mListener != null) {
            mListener.onItemClicked(ITEM_OPEN);
        }

        dismiss();
    }

    //Click on Take Attendance
    @OnClick(R.id.takeAttendanceItem)
    public void takeAttendance(View view) {
        if (mListener != null) {
            mListener.onItemClicked(ITEM_TAKE_ATTENDANCE);
        }

        dismiss();
    }

    @OnClick(R.id.exportPdf)
    public void exportAsPdf(View view) {
        if (mListener != null) {
            mListener.onItemClicked(ITEM_EXPORT_PDF);
        }

        dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.course_bottom_sheet_layout, container, false);

        ButterKnife.bind(this, view);

        return view;
    }


}
