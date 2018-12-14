package com.example.isslocator.model;

public class Result {
    private final String duration;
    private final String time;
    private final String latLong;

    public Result(String duration, String time, String latLong) {
        this.duration = duration;
        this.time = time;
        this.latLong = latLong;
    }

    public String getDuration() {
        return duration;
    }

    public String getTime() {
        return time;
    }

    public String getLatLong() {
        return latLong;
    }
}
