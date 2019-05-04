package com.saurov.attendancemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.database.CourseStudent;
import com.vaibhavlakhera.circularprogressview.CircularProgressView;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {

    private List<CourseStudent> courseStudents;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public interface OnItemClickListener {
        void onClick(CourseStudent student, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public StudentAdapter(Context context, List<CourseStudent> courseStudents) {
        this.context = context;
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

        //Randomly generating for testing

        Random random = new Random(System.currentTimeMillis());

        student.setAttendancePercentage(random.nextInt(100));

        student.setAttendanceMark(student.percentageToMark(student.getAttendancePercentage()));

        holder.attendancePercentageCircularProgress.setProgress(student.getAttendancePercentage(), true);
        holder.attendanceMarkCircularProgress.setProgress(student.getAttendanceMark(), true);


        holder.attendancePercentageCircularProgress
                .setProgressColor(getAttendancePercentageProgressColor(student.getAttendancePercentage()));

        holder.attendanceMarkCircularProgress
                .setProgressColor(getAttendanceMarkProgressColor(student.getAttendanceMark()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onItemClickListener != null) {
                    onItemClickListener.onClick(student, position);
                }
            }
        });

    }

    private int getAttendancePercentageProgressColor(int attendancePercentage) {

        if (attendancePercentage == 100) {
            return ContextCompat.getColor(context, R.color.progress_excellent);
        } else if (attendancePercentage >= 70) {
            return ContextCompat.getColor(context, R.color.progress_good);
        } else if (attendancePercentage >= 50) {
            return ContextCompat.getColor(context, R.color.progress_average);
        } else {
            return ContextCompat.getColor(context, R.color.progress_bad);
        }

    }

    private int getAttendanceMarkProgressColor(int attendanceMark) {

        if (attendanceMark == 8) {
            return ContextCompat.getColor(context, R.color.progress_excellent);
        } else if (attendanceMark >= 6) {
            return ContextCompat.getColor(context, R.color.progress_good);
        } else if (attendanceMark >= 4) {
            return ContextCompat.getColor(context, R.color.progress_average);
        } else {
            return ContextCompat.getColor(context, R.color.progress_bad);
        }

    }

    @Override
    public int getItemCount() {
        return courseStudents.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.student_roll)
        TextView studentRoll;

        @BindView(R.id.attendance_mark_circular_progress)
        CircularProgressView attendanceMarkCircularProgress;


        @BindView(R.id.attendance_percentage_circular_progress)
        CircularProgressView attendancePercentageCircularProgress;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
