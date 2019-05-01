package com.saurov.attendancemanager.database;

import com.orm.SugarRecord;

public class CourseStudent extends SugarRecord<CourseStudent> {


    // Each CourseStudent Belongs to A Course
    private Course course;
    private int roll;
    private double attendancePercentage;
    private int attendanceMark;


    public CourseStudent() {
        attendancePercentage = 0;
        attendanceMark = 0;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public int getAttendanceMark() {
        return attendanceMark;
    }

    public void setAttendanceMark(int attendanceMark) {
        this.attendanceMark = attendanceMark;
    }
}
