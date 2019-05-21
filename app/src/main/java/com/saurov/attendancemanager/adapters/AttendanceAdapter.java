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
    private onAttendanceCheckboxClickedListener listener;
//    private boolean isSelectedAll = false;

    public void selectAllStudents() {

        StringBuilder sb = new StringBuilder();

        for (CourseStudent s : selectedStudentList) {
            sb.append(s.getRoll()).append(", ");
        }

        Log.d("TAG1", sb.toString());

        selectedStudentList.clear();

//        selectedStudentList.add(studentList.get(0));
//        selectedStudentList.add(studentList.get(3));
        selectedStudentList.addAll(studentList);
//        isSelectedAll = true;
        notifyDataSetChanged();

        sb = new StringBuilder();

        for (CourseStudent s : selectedStudentList) {
            sb.append(s.getRoll()).append(", ");
        }

        Log.d("TAG2", sb.toString());

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

        StringBuilder sb = new StringBuilder();

        for (CourseStudent s : selectedStudentList) {
            sb.append(s.getRoll()).append(", ");
        }

        Log.d("onBind", sb.append("  " + position).toString());

        CourseStudent student = studentList.get(position);

        holder.studentRoll.setText(Integer.toString(student.getRoll()));

        if (selectedStudentList.contains(student)) {
            holder.attendanceCheckBox.setChecked(true);
        } else {
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

                Log.d("CHECK", position + " ");

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
