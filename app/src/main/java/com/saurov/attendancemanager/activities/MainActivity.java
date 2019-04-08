 package com.saurov.attendancemanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saurov.attendancemanager.adapters.CategoryAdapter;
import com.saurov.attendancemanager.R;

 public class MainActivity extends AppCompatActivity {

     private CategoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView categories = findViewById(R.id.category_list);
        mAdapter = new CategoryAdapter(this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        categories.setLayoutManager(mLayoutManager);
        categories.setItemAnimator(new DefaultItemAnimator());
        categories.setAdapter(mAdapter);
    }
}
