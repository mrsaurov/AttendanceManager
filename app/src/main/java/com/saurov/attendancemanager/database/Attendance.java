package com.saurov.attendancemanager.database;

import com.orm.SugarRecord;

import java.util.Objects;

public class Attendance extends SugarRecord<Attendance> {

    private CourseStudent courseStudent;
    private CourseClass courseClass;

    public Attendance() {

    }

    public Attendance(CourseStudent courseStudent, CourseClass courseClass) {
        this.courseStudent = courseStudent;
        this.courseClass = courseClass;
    }

    public CourseStudent getCourseStudent() {
        return courseStudent;
    }

    public void setCourseStudent(CourseStudent courseStudent) {
        this.courseStudent = courseStudent;
    }

    public CourseClass getCourseClass() {
        return courseClass;
    }

    public void setCourseClass(CourseClass courseClass) {
        this.courseClass = courseClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendance that = (Attendance) o;
        return Objects.equals(courseStudent, that.courseStudent) &&
                Objects.equals(courseClass, that.courseClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseStudent, courseClass);
    }
}
