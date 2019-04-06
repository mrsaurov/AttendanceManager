package com.saurov.attendancemanager.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.database.Course;
import com.saurov.attendancemanager.dialogs.BottomSheetDialogFrament;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {

    private Context context;
    private List<Course> courseList;
    private OnItemClickListener onItemClickListener;



    public interface OnItemClickListener {
        void onClick(Course course, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


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

        myViewHolder.courseMenuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment bottomSheet = new BottomSheetDialogFrament();


                bottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), "example");
            }
        });

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onItemClickListener != null) {
                    onItemClickListener.onClick(course, i);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView courseNo;
        TextView courseTitle;
        TextView courseClass;
        ImageView courseMenuImageView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            courseNo = itemView.findViewById(R.id.course_no);
            courseTitle = itemView.findViewById(R.id.course_title);
            courseClass = itemView.findViewById(R.id.course_class);
            courseMenuImageView = itemView.findViewById(R.id.course_menu_image_view);

        }
    }
}
