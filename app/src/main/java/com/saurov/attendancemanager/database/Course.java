package com.saurov.attendancemanager.database;

import com.orm.SugarRecord;

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
}
