package com.saurov.attendancemanager.database;

import com.orm.SugarRecord;

import java.util.List;

public class Course extends SugarRecord<Course> {

    private String number;
    private String title;
    private String series;
    private String department;
    private String section;
    private int totalClassTaken;

    public Course() {
        totalClassTaken = 0;
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

    public List<CourseClass> getAllClasses() {

        return SugarRecord.findWithQuery(CourseClass.class,
                "select distinct course_class.id, day, cycle, timestamp " +
                        "from course_class,attendance,course_student " +
                        "where attendance.course_class = course_class.id and " +
                        "course_student.id = attendance.course_student and course_student.course = ?",
                String.valueOf(this.getId()));
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
