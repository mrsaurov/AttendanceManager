package com.saurov.attendancemanager.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.saurov.attendancemanager.activites.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);

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
