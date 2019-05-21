package com.saurov.attendancemanager.database;

import com.orm.SugarRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CourseClass extends SugarRecord<CourseClass> {
    private String day;
    private String cycle;
    private long timestamp;

    public CourseClass() {

    }

    public CourseClass(String day, String cycle, long timestamp) {
        this.day = day;
        this.cycle = cycle;
        this.timestamp = timestamp;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getHumanReadableDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM, yyyy", Locale.US);

        return sdf.format(new Date(this.getTimestamp()));
    }

    public long getTotalStudentPresent() {
        return SugarRecord.count(Attendance.class, "course_class =?", new String[]{String.valueOf(this.getId())});
    }

}
