package com.example.mywork2.domain;
/**
 * @author Jing
 * function: used for store a temp ticket object in the program
 */
public class Ticket {
    private String ticketId;
    private String castleName;
    private String username;
    private String journeyId;
    private String date;
    private String time;
    private String returnTime;
    private int quantity;
    private int totalPrice;
    private boolean isPaid;

    public Ticket(String ticketId, String castleName, String username, String journeyId, String date, String time, String returnTime, int quantity, int totalPrice, boolean isPaid) {
        this.ticketId = ticketId;
        this.castleName = castleName;
        this.username = username;
        this.journeyId = journeyId;
        this.date = date;
        this.time = time;
        this.returnTime = returnTime;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.isPaid = isPaid;
    }

    public int getSinglePrice(){
        return totalPrice / quantity;
    }

    //remove part but not all tickets
    //if the num >= left num
    //return -1
    //else return the left num
    public int removePartTickets(int num){
        if(num > quantity) return -1;
        totalPrice -= getSinglePrice() * num;
        quantity -= num;
        return quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
