package com.saurov.attendancemanager.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.database.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {

    private Context context;
    private List<Course> courseList;

    public CourseAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.course_list_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Course course = courseList.get(i);

        myViewHolder.courseTitle.setText(course.getTitle());
        myViewHolder.courseNo.setText(course.getNumber());
        myViewHolder.courseClass.setText(
                course.getDepartment() + " " + course.getSeries() + " " + course.getSection());

    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView courseNo;
        TextView courseTitle;
        TextView courseClass;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            courseNo = itemView.findViewById(R.id.course_no);
            courseTitle = itemView.findViewById(R.id.course_title);
            courseClass = itemView.findViewById(R.id.course_class);

        }
    }
}
