package com.saurov.attendancemanager.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, CourseActivity.class);

        synchronized (this) {

            try {
                wait(500);
            } catch (InterruptedException e) {
                startActivity(intent);
            }
        }

        startActivity(intent);
        finish();
    }
}
