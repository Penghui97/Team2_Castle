package com.example.mywork2.domain;

public class Ticket {
    private String ticketId;
    private String castleName;
    private String username;
    private String journeyId;
    private String date;
    private String time;
    private String returnTime;
    private int adultQuantity;
    private int kidsQuantity;
    private int totalPrice;
    private boolean isPaid;

    public Ticket(String ticketId, String castleName, String username, String journeyId, String date, String time, String returnTime, int adultQuantity, int kidsQuantity, int totalPrice) {
        this.ticketId = ticketId;
        this.castleName = castleName;
        this.username = username;
        this.journeyId = journeyId;
        this.date = date;
        this.time = time;
        this.returnTime = returnTime;
        this.adultQuantity = adultQuantity;
        this.kidsQuantity = kidsQuantity;
        this.totalPrice = totalPrice;
        this.isPaid = false;
    }

    public Ticket(String ticketId, String castleName, String username, String journeyId, String date, String time, String returnTime, int adultQuantity, int kidsQuantity, int totalPrice, boolean isPaid) {
        this.ticketId = ticketId;
        this.castleName = castleName;
        this.username = username;
        this.journeyId = journeyId;
        this.date = date;
        this.time = time;
        this.returnTime = returnTime;
        this.adultQuantity = adultQuantity;
        this.kidsQuantity = kidsQuantity;
        this.totalPrice = totalPrice;
        this.isPaid = isPaid;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getCastleName() {
        return castleName;
    }

    public void setCastleName(String castleName) {
        this.castleName = castleName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public int getAdultQuantity() {
        return adultQuantity;
    }

    public void setAdultQuantity(int adultQuantity) {
        this.adultQuantity = adultQuantity;
    }

    public int getKidsQuantity() {
        return kidsQuantity;
    }

    public void setKidsQuantity(int kidsQuantity) {
        this.kidsQuantity = kidsQuantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String briefInfo() {
        return  castleName + " : " + date + " " +
                time + " - " + returnTime;
    }
}
