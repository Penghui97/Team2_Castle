package com.example.mywork2.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @author Jing
 * function: used for store a temp departureTime object in the program
 */
public class DepartureTime {
    private String routeId;
    private int depNo;
    private String depTime;

    public DepartureTime(String routeId, int depNo, String depTime) {
        this.routeId = routeId;
        this.depNo = depNo;
        this.depTime = depTime;
    }

    public String addMinutes(int minutes){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String res = null;
        try {
            Date dateTime = format.parse(depTime);
            dateTime.setMinutes((dateTime.getMinutes() + minutes) % 60);
            dateTime.setHours((dateTime.getMinutes() + minutes) / 60);
            res = format.format(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public int getDepNo() {
        return depNo;
    }

    public void setDepNo(int depNo) {
        this.depNo = depNo;
    }

    public String getDepTime() {
        return depTime;
    }

    public void setDepTime(String depTime) {
        this.depTime = depTime;
    }
}
