package com.saurov.attendancemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.database.CourseStudent;
import com.vaibhavlakhera.circularprogressview.CircularProgressView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> implements Filterable {

    private List<CourseStudent> courseStudentsList;
    private List<CourseStudent> courseStudentsListFull;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public interface OnItemClickListener {
        void onClick(CourseStudent student, int position);

        void onMenuClick(CourseStudent student, int position);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public StudentAdapter(Context context, List<CourseStudent> courseStudentsList) {
        this.context = context;
        this.courseStudentsList = courseStudentsList;
        this.courseStudentsListFull = new ArrayList<>(courseStudentsList);
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

        CourseStudent student = courseStudentsList.get(position);

        holder.studentRoll.setText(String.valueOf(student.getRoll()));

        double attendancePercentage = student.getAttendancePercentage();

        holder.attendancePercentageCircularProgress.setProgress((int) Math.round(attendancePercentage), false);
        holder.attendanceMarkCircularProgress.setProgress(student.getAttendanceMark(), false);


        holder.attendancePercentageCircularProgress
                .setProgressColor(getAttendancePercentageProgressColor(attendancePercentage));

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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onMenuClick(student, position);
                }
                return true;
            }
        });

    }

    public void refreshData(List<CourseStudent> data) {
        this.courseStudentsList = data;
    }

    private int getAttendancePercentageProgressColor(double attendancePercentage) {

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
    public Filter getFilter() {
        return studentFilter;
    }

    private Filter studentFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CourseStudent> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(courseStudentsListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (CourseStudent item : courseStudentsListFull) {
                    if (String.valueOf(item.getRoll()).contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            courseStudentsList.clear();
            courseStudentsList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    @Override
    public int getItemCount() {
        return courseStudentsList.size();
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
