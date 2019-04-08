package com.saurov.attendancemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.database.Course;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {

    private Context context;
    private List<Course> courseList;
    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onClick(Course course, int position);

        void onMenuClick(Course course, int position);
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
//        myViewHolder.courseNo.setText(course.getNumber());
        myViewHolder.courseClass.setText(
                course.getDepartment() + " " + course.getSeries() + " " + course.getSection());

        myViewHolder.courseMenuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onItemClickListener != null) {
                    onItemClickListener.onMenuClick(course, i);
                }
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

        myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onMenuClick(course, i);
                }

                return true;
            }
        });

        ColorGenerator generator = ColorGenerator.MATERIAL;

        int color = generator.getColor(course.getSeries());

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(course.getSeries(), color);

        myViewHolder.courseSeriesImageView.setImageDrawable(drawable);


    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.course_no)
//        TextView courseNo;
        @BindView(R.id.course_series_image_view)
        ImageView courseSeriesImageView;

        @BindView(R.id.course_title)
        TextView courseTitle;

        @BindView(R.id.course_class)
        TextView courseClass;

        @BindView(R.id.course_menu_image_view)
        ImageView courseMenuImageView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }
}
