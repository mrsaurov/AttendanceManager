package com.saurov.attendancemanager.database;

import com.orm.SugarRecord;

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


}
