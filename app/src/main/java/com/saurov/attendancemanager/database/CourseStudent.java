package com.saurov.attendancemanager.database;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.Random;

public class CourseStudent extends SugarRecord<CourseStudent> {


    // Each CourseStudent Belongs to A Course
    private Course course;
    private int roll;
    private int attendancePercentage;
    private int attendanceMark;

    @Ignore
    private boolean isSelected;

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public CourseStudent() {

        isSelected = false;
        attendancePercentage = 0;
        attendanceMark = 0;

    }

    public int percentageToMark(int attendancePercentage) {

        int obtainedMark;

        if (attendancePercentage >= 90) {
            obtainedMark = 8;
        } else if (attendancePercentage >= 85) {
            obtainedMark = 7;
        } else if (attendancePercentage >= 80) {
            obtainedMark = 6;
        } else if (attendancePercentage >= 70) {
            obtainedMark = 5;
        } else if (attendancePercentage >= 60) {
            obtainedMark = 4;
        } else {
            obtainedMark = 0;
        }

        return obtainedMark;
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

    public int getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(int attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public int getAttendanceMark() {
        return attendanceMark;
    }

    public void setAttendanceMark(int attendanceMark) {
        this.attendanceMark = attendanceMark;
    }
}
