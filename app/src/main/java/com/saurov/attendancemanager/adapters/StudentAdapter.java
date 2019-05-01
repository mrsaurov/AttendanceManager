package com.saurov.attendancemanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.database.CourseStudent;
import com.vaibhavlakhera.circularprogressview.CircularProgressView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {

    private List<CourseStudent> courseStudents;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(CourseStudent student, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public StudentAdapter(List<CourseStudent> courseStudents) {
        this.courseStudents = courseStudents;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_list_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CourseStudent student = courseStudents.get(position);

        holder.studentRoll.setText(Integer.toString(student.getRoll()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onItemClickListener != null) {
                    onItemClickListener.onClick(student, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return courseStudents.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.student_roll)
        TextView studentRoll;

        @BindView(R.id.attendance_percentage_circular_progress)
        CircularProgressView attendancePercentageCircularProgress;

        @BindView(R.id.attendance_mark_circular_progress)
        CircularProgressView attendanceMarkCircularProgress;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
