package com.saurov.attendancemanager.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.views.RoundedBottomSheetDialogFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClassBottomSheetDialogFragment extends RoundedBottomSheetDialogFragment {


    public interface BottomSheetListener {
        void onItemClicked(String selectionId);
    }

    private BottomSheetListener mListener;


    public static final String ITEM_EDIT = "ClassBottomSheetDialogFragment.EDIT";
    public static final String ITEM_DELETE = "ClassBottomSheetDialogFragment.DELETE";
    public static final String ITEM_OPEN = "ClassBottomSheetDialogFragment.OPEN";


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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.class_bottom_sheet_layout, container, false);

        ButterKnife.bind(this, view);

        return view;
    }


}
