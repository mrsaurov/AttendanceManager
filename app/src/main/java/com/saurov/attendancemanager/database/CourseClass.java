package com.saurov.attendancemanager.database;

import com.orm.SugarRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CourseClass extends SugarRecord<CourseClass> {
    private String day;
    private String cycle;
    private long timestamp;

    public CourseClass() {

    }

    public CourseClass(String day, String cycle, long timestamp) {
        this.day = day;
        this.cycle = cycle;
        this.timestamp = timestamp;
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

    public String getHumanReadableDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM, yyyy", Locale.US);

        return sdf.format(new Date(this.getTimestamp()));
    }

    public long getTotalStudentPresent() {
        return SugarRecord.count(Attendance.class,
                "course_class =?", new String[]{String.valueOf(this.getId())});
    }

    public List<CourseStudent> getAttendedStudents() {

        return SugarRecord
                .findWithQuery(CourseStudent.class, "select course_student.id, attendance_mark, attendance_percentage, course, roll" +
                                " from course_student, attendance" +
                                " where course_student = course_student.id and course_class = ?",
                        String.valueOf(this.getId()));

    }

    public void deleteAllAttendance(){
        SugarRecord.deleteAll(Attendance.class, "course_class = ?", String.valueOf(this.getId()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseClass that = (CourseClass) o;
        return timestamp == that.timestamp &&
                Objects.equals(day, that.day) &&
                Objects.equals(cycle, that.cycle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, cycle, timestamp);
    }
}
