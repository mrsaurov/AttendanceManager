package com.saurov.attendancemanager.database;

import com.orm.SugarRecord;

public class Class{
    private String day;
    private String cycle;
    private long timestamp;

    public Class(String day, String cycle, long timestamp) {
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
}
