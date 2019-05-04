package com.saurov.attendancemanager.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.database.CourseStudent;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder> {

    public interface onAttendanceCheckboxClickedListener {

        void onClick(CourseStudent student, boolean isChecked,
                     List<CourseStudent> selectedStudents, CustomCheckBox checkbox, int position);
    }

    private Context context;
    private List<CourseStudent> studentList;
    private List<CourseStudent> selectedStudentList;
    onAttendanceCheckboxClickedListener listener;
//    private boolean isSelectedAll = false;

    public void selectAllStudents() {
        selectedStudentList.clear();
        selectedStudentList.addAll(studentList);
//        isSelectedAll = true;
        notifyDataSetChanged();
    }

    public void clearAllStudents() {
        selectedStudentList.clear();
//        isSelectedAll = false;
        notifyDataSetChanged();
    }


    public AttendanceAdapter(Context context, List<CourseStudent> studentList) {
        this.context = context;
        this.studentList = studentList;
        this.selectedStudentList = new ArrayList<>();
    }

    public void setOnAttendanceCheckboxClickedListener(onAttendanceCheckboxClickedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_list_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CourseStudent student = studentList.get(position);

        holder.studentRoll.setText(Integer.toString(student.getRoll()));

        if (selectedStudentList.contains(student)){
            holder.attendanceCheckBox.setChecked(true);
        }else {
            holder.attendanceCheckBox.setChecked(false);
        }

//        if (isSelectedAll){
//            holder.attendanceCheckBox.setChecked(true);
//        }else {
//            holder.attendanceCheckBox.setChecked(false);
//        }

        holder.attendanceCheckBox.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {

                if (listener != null) {
                    listener.onClick(student, isChecked, selectedStudentList, checkBox, position);
                }

            }
        });


    }

    public List<CourseStudent> getSelectedStudentList() {
        return selectedStudentList;
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.attendance_checkbox)
        CustomCheckBox attendanceCheckBox;

        @BindView(R.id.student_roll)
        TextView studentRoll;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
