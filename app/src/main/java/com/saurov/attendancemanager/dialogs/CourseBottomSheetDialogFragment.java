package com.saurov.attendancemanager.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saurov.attendancemanager.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CourseBottomSheetDialogFragment extends RoundedBottomSheetDialogFragment {


    public static final String ITEM_EDIT = "CourseBottomSheetDialogFragment.EDIT";
    public static final String ITEM_DELETE = "CourseBottomSheetDialogFragment.DELETE";
    public static final String ITEM_OPEN = "CourseBottomSheetDialogFragment.OPEN";


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
    }


    public interface BottomSheetListener {
        void onItemClicked(String selectionId);
    }

    private BottomSheetListener mListener;

    public void setOnItemClickListener(BottomSheetListener listener) {
        this.mListener = listener;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        try {
//            mListener = (BottomSheetListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString()
//                    + " must implement BottomSheetListener");
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        ButterKnife.bind(this, view);

        return view;
    }


}