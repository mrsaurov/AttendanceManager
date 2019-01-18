 package com.saurov.attendancemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

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
