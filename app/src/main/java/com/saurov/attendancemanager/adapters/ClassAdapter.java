package com.saurov.attendancemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.database.CourseClass;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.MyViewHolder> {

    public interface onItemClickListener {
        void onClick(CourseClass courseClass, int position);

        void onMenuClick(CourseClass courseClass, int position);
    }

    private Context context;
    private List<CourseClass> courseClassList;
    private onItemClickListener listener;

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public ClassAdapter(Context context, List<CourseClass> courseClassList) {
        this.context = context;
        this.courseClassList = courseClassList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.class_list_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CourseClass courseClass = courseClassList.get(position);

        String className = courseClass.getCycle() + courseClass.getDay();

        ColorGenerator generator = ColorGenerator.MATERIAL;

        int color = generator.getColor(courseClass.getCycle());
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(className, color);

        holder.courseNameImageView.setImageDrawable(drawable);
        holder.classTimeTextView.setText(courseClass.getHumanReadableDate());
        holder.classSummaryTextView.setText("Student Present: " + courseClass.getTotalStudentPresent());

        if (listener != null) {
            holder.classMenuImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMenuClick(courseClass, position);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(courseClass, position);
                }
            });
        }

    }

    public void refreshData(List<CourseClass> data) {
        this.courseClassList = data;
    }

    @Override
    public int getItemCount() {
        return courseClassList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.class_name_image_view)
        ImageView courseNameImageView;

        @BindView(R.id.class_menu_image_view)
        ImageView classMenuImageView;

        @BindView(R.id.class_time)
        TextView classTimeTextView;

        @BindView(R.id.class_date_text_view)
        TextView classSummaryTextView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
