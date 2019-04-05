package com.saurov.attendancemanager.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.activites.CourseActivity;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private Context context;
    private List<String> categoryList;
//    private List<Integer>

    public CategoryAdapter(Context context) {

        this.context = context;

        categoryList = new ArrayList<>();
        categoryList.add("Courses");
        categoryList.add("Stats");
        categoryList.add("Settings");
        categoryList.add("About");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.category_list_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {

        RelativeLayout relativeLayout = holder.itemView.findViewById(R.id.color_layout);

        switch (i) {

            case 0:
                relativeLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.fixture_gradient));
//                relativeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.fixture_color));
                holder.thumbnail.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.course));
                break;

            case 1:
                relativeLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.team_gradient));
//                relativeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.teams_color));
                holder.thumbnail.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.statistics));
                break;


            case 2:
                relativeLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.points_gradient));
//                relativeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.points_color));
                holder.thumbnail.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.settings));
                break;


            case 3:
                relativeLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.venue_gradient));
//                relativeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.venue_color));
                holder.thumbnail.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.about));
                break;


        }


        holder.title.setText(categoryList.get(i));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (categoryList.get(i).equals("Courses")) {

                    Intent intent = new Intent(context, CourseActivity.class);

                    context.startActivity(intent);

                } else {

                    Toast.makeText(context, categoryList.get(i), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            thumbnail = itemView.findViewById(R.id.thumbnail);

        }
    }

}
