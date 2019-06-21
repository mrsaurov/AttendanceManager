package com.saurov.attendancemanager.database;

import com.orm.SugarRecord;

import java.util.List;
import java.util.Objects;

public class Course extends SugarRecord<Course> {

    private String number;
    private String title;
    private String series;
    private String department;
    private String section;

    public Course() {
    }

    public Course(String number, String title, String series, String department, String section) {
        this.number = number;
        this.title = title;
        this.series = series;
        this.department = department;
        this.section = section;
    }

    public List<CourseStudent> getStudents() {
        return SugarRecord.find(CourseStudent.class, "course = ?", String.valueOf(this.getId()));
    }

//    public List<CourseClass> getAllClasses() {
//        return SugarRecord.find(CourseClass.class, "course = ?", String.valueOf(this.getId()));
//
//    }

    public List<CourseClass> getAllClasses(){
        return SugarRecord.find(CourseClass.class, "course = ?", new String[]{String.valueOf(this.getId())}, null,"timestamp desc",null);
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

        List<CourseClass> courseClasses = this.getAllClasses();

        int totalClassTaken = 0;

        for (CourseClass courseclass : courseClasses) {

            totalClassTaken += courseclass.getPeriod();
        }

        return totalClassTaken;
    }

//    public void setTotalClassTaken(int totalClassTaken) {
//        this.totalClassTaken = totalClassTaken;
//    }

    public long getTotalStudents() {
        return SugarRecord.count(CourseStudent.class, "course = ?", new String[]{String.valueOf(this.getId())});
    }

    public CourseClass getLastClassTaken() {

        return SugarRecord.find(CourseClass.class, "course = ?",
                new String[]{String.valueOf(this.getId())}, null, "timestamp desc", "1").get(0);
    }

    public void deleteCascade() {

        //Delete all course class
        SugarRecord.deleteAll(CourseClass.class, "course = ?", String.valueOf(this.getId()));

        //Delete all attendance data
        SugarRecord.executeQuery("delete from attendance where " +
                "course_student in " +
                "(select id from course_student where course = ?)", String.valueOf(this.getId()));

        //Delete All Students
        SugarRecord.deleteAll(CourseStudent.class, "course = ?", String.valueOf(this.getId()));

        //Delete this course
        this.delete();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(number, course.number) &&
                Objects.equals(title, course.title) &&
                Objects.equals(series, course.series) &&
                Objects.equals(department, course.department) &&
                Objects.equals(section, course.section);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, title, series, department, section);
    }
}
