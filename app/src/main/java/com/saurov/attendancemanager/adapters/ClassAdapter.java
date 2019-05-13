package com.saurov.attendancemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.database.Attendance;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.MyViewHolder> {

    private Context context;
    private List<Attendance> classAttendance;

    public ClassAdapter(Context context, List<Attendance> classAttendance) {
        this.context = context;
        this.classAttendance = classAttendance;
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

        Attendance attendance = classAttendance.get(position);

        ColorGenerator generator = ColorGenerator.MATERIAL;

        int color = generator.getColor(attendance.getCycle());

        String text = attendance.getCycle() + attendance.getDay();

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(text, color);

        holder.courseSeriesImageView.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.course_series_image_view)
        ImageView courseSeriesImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
