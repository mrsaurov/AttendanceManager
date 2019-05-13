package com.saurov.attendancemanager.database;

import android.util.Log;

import com.orm.SugarCursorFactory;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

public class Course extends SugarRecord<Course> {

    private String number;
    private String title;
    private String series;
    private String department;
    private String section;
    private int totalClassTaken;

    public Course() {
    }

    public Course(String number, String title, String series, String department, String section) {
        this.number = number;
        this.title = title;
        this.series = series;
        this.department = department;
        this.section = section;
        this.totalClassTaken = 0;
    }

    public List<CourseStudent> getStudents() {
        return SugarRecord.find(CourseStudent.class, "course = ?", String.valueOf(this.getId()));
    }

    public List<Attendance> getAllClasses() {

        List<Attendance> classes =
                Select.from(Attendance.class).groupBy("cycle,day").list();
        Log.d("Saurov", "getAllClasses: " + classes.get(2).getDay() + classes.get(2).getCycle());

        return classes;
//        return SugarRecord.find(Attendance.class, "course_id = ?", String.valueOf(this.getId()));
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getTotalClassTaken() {
        return totalClassTaken;
    }

    public void setTotalClassTaken(int totalClassTaken) {
        this.totalClassTaken = totalClassTaken;
    }
}
