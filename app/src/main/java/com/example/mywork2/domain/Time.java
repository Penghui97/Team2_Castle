package com.example.mywork2.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @author Jing
 * function: used for store a temp time object in the program
 */
public class Time {
    private int hour;
    private int minute;
    private int second;

    public Time(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public Time(String strTime) {
        String[] times = strTime.split(":");
        this.hour = Integer.parseInt(times[0]);
        this.minute = Integer.parseInt(times[1]);
        if (times.length == 3){
            this.second = Integer.parseInt(times[2]);
        }
    }

    public Time(Time time){
        this.hour = time.getHour();
        this.minute = time.getMinute();
        this.second = time.getSecond();
    }

    public String add(int minutes) {
        this.minute += minutes;
        this.hour += this.minute / 60;
        this.minute = this.minute % 60;
        return this.toString();
    }

    public String reduce(int minutes){
        this.minute -= minutes;
        if(this.minute < 0){
            this.hour -= (this.minute / 60 + 1);
            this.minute = this.minute % 60 + 60;
        }
        return this.toString();
    }

    public String setTime(String time) {
        String[] times = time.split(":");
        this.hour = Integer.parseInt(times[0]);
        this.minute = Integer.parseInt(times[1]);
        if (times.length == 3){
            this.second = Integer.parseInt(times[2]);
        }
        return this.toString();
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public String toString() {
        String strHour = hour + "";
        String strMinute = minute + "";
        if(hour < 10){
            strHour = "0" + strHour;
        }
        if(minute < 10){
            strMinute = "0" + strMinute;
        }
        return strHour + ":" + strMinute;
    }
}
