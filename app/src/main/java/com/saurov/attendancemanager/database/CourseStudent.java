package com.saurov.attendancemanager.database;

import android.util.LongSparseArray;
import android.util.SparseBooleanArray;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CourseStudent extends SugarRecord<CourseStudent> {


    // Each CourseStudent Belongs to A Course
    private Course course;
    private int roll;
    // TODO: 2019-05-30 Make sure percentage and
    //  mark is always up to date even when course class is deleted.
    //  This issue is caused because these values are set when taking attendance
    //  and never retrieved using sql query as a result deleting class wont reflect on percentage and mark
    private double attendancePercentage;
    private int attendanceMark;

    public CourseStudent() {

        attendancePercentage = 0;
        attendanceMark = 0;

    }

    public int percentageToMark(double attendancePercentage) {

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

    public long getTotalClassAttended() {

        return SugarRecord.count(Attendance.class,
                "course_student = ?", new String[]{String.valueOf(this.getId())});
    }

    public List<CourseClass> getClassesAttended() {

        return SugarRecord.findWithQuery(CourseClass.class,
                "select course_class.course,day,cycle,timestamp,period from course_class,attendance " +
                        "where course_class.id = attendance.course_class and course_student = ?", String.valueOf(this.getId()));

    }

    public List<CourseClass> getClassesAbsent() {

        return SugarRecord.findWithQuery(CourseClass.class,
                "select * from course_class where id not in (select course_class.id from course_class,attendance " +
                        "where course_class.id = attendance.course_class and course_student = ?) and course = ?", String.valueOf(this.getId()), String.valueOf(course.getId()));



//        HashSet<Long> presentClassesId = new HashSet<>();
//
//        List<CourseClass> absentList = new ArrayList<>();
//
//        for (CourseClass courseClass : getClassesAttended()) {
//            presentClassesId.add(courseClass.getId());
//        }
//
//        for (CourseClass courseClass : course.getAllClasses()) {
//            if (!presentClassesId.contains(courseClass.getId())) {
//                absentList.add(courseClass);
//            }
//        }
//
//        return absentList;
    }

    public void deleteCascade() {

        //Delete all attendance
        SugarRecord.deleteAll(Attendance.class, "course_student = ?", String.valueOf(this.getId()));

        //Delete this student
        this.delete();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseStudent student = (CourseStudent) o;
        return roll == student.roll &&
                Double.compare(student.attendancePercentage, attendancePercentage) == 0 &&
                attendanceMark == student.attendanceMark &&
                Objects.equals(course, student.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, roll, attendancePercentage, attendanceMark);
    }
}
