package com.saurov.attendancemanager.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saurov.attendancemanager.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BottomSheetDialogFrament extends RoundedBottomSheetDialogFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.bottom_sheet_layout, container, false);
    }
}
