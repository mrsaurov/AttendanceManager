package com.saurov.attendancemanager.adapters;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.database.CourseStudent;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttendanceAdapter2 extends RecyclerView.Adapter<AttendanceAdapter2.MyViewHolder> {

    private final SparseBooleanArray array = new SparseBooleanArray();
    private List<CourseStudent> selectedStudents = new ArrayList<>();


    public interface onAttendanceCheckboxClickedListener {

        void onClick(CourseStudent student, boolean isChecked,
                     List<CourseStudent> selectedStudents, CustomCheckBox checkbox, int position);
    }

    private Context context;
    private List<CourseStudent> studentList;
    private onAttendanceCheckboxClickedListener listener;
    private boolean isSelectedAll = false;

    public void selectAllStudents() {

        selectedStudents.clear();
        isSelectedAll = true;
        selectedStudents.addAll(studentList);

        notifyDataSetChanged();
    }

    public void clearAllStudents() {
        isSelectedAll = false;
        array.clear();
        selectedStudents.clear();
        notifyDataSetChanged();
    }


    public List<CourseStudent> getSelectedStudents() {
        return selectedStudents;
    }

    public AttendanceAdapter2(Context context, List<CourseStudent> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    public AttendanceAdapter2(Context context, List<CourseStudent> studentList, List<CourseStudent> selectedStudents) {
        this.context = context;
        this.studentList = studentList;
        this.selectedStudents = selectedStudents;

        for (int position = 0; position < studentList.size(); position++) {

            CourseStudent student = studentList.get(position);

            if (selectedStudents.contains(student)) {
                array.put(position, true);
                Log.d("XX", "AttendanceAdapter2:yes " +student.getRoll());
            } else {
                array.put(position, false);
                Log.d("XX", "AttendanceAdapter2:no " +student.getRoll());
            }
        }

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

        holder.bind(position);
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


            attendanceCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();

                    if (!array.get(adapterPosition, false)) {
                        attendanceCheckBox.setChecked(true, true);
                        selectedStudents.add(studentList.get(adapterPosition));
                        isSelectedAll = false;

                        StringBuilder sb = new StringBuilder();
                        for (CourseStudent cs : selectedStudents) {
                            sb.append(cs.getRoll()).append(", ");
                        }

                        Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show();

                        array.put(adapterPosition, true);
                    } else {
                        attendanceCheckBox.setChecked(false, true);
                        selectedStudents.remove(studentList.get(adapterPosition));
                        isSelectedAll = false;
                        StringBuilder sb = new StringBuilder();
                        for (CourseStudent cs : selectedStudents) {
                            sb.append(cs.getRoll()).append(", ");
                        }

                        Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show();

                        array.put(adapterPosition, false);
                    }
                }
            });
        }

        void bind(int position) {

            studentRoll.setText(String.valueOf(studentList.get(position).getRoll()));

            if (isSelectedAll) {
                array.put(position, true);
            }
            // use the sparse boolean array to check
            if (!array.get(position, false)) {
                attendanceCheckBox.setChecked(false);
            } else {
                attendanceCheckBox.setChecked(true);
            }

        }
    }
}

