package com.saurov.attendancemanager.database;

import com.orm.SugarRecord;

public class Attendance extends SugarRecord<Attendance> {

    private CourseStudent courseStudent;
    private String day;
    private String cycle;
    private long timestamp;

    public Attendance() {
    }

    public Attendance(CourseStudent courseStudent, String day, String cycle, long timestamp) {
        this.courseStudent = courseStudent;
        this.day = day;
        this.cycle = cycle;
        this.timestamp = timestamp;
    }

    public CourseStudent getCourseStudent() {
        return courseStudent;
    }

    public void setCourseStudent(CourseStudent courseStudent) {
        this.courseStudent = courseStudent;
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
}
