package com.example.mywork2.domain;

import java.util.ArrayList;

public class Journey {
    private String journeyId;
    private Castle castle;
    private ArrayList<Route> routes;
    private ArrayList<Route> returnRoutes;

    public Journey(String journeyId, Castle castle, ArrayList<Route> routes, ArrayList<Route> returnRoutes) {
        this.journeyId = journeyId;
        this.castle = castle;
        this.routes = routes;
        this.returnRoutes = returnRoutes;
    }

    public Journey() {}

    //get the departure position
    public String getDeparture(){
        for(Route route : routes){
            if(route.getLed() == 1) return route.getStart();
        }
        return "Error";
    }
    //get the single journey duration
    public int getSingleDuration(){
        int res = 0;
        for(Route route : routes){
            res += route.getDuration();
        }
        return res;
    }
    //get the single journey price
    public int getSinglePrice(){
        int res = 0;
        for(Route route : routes){
            res += route.getAdultPrice();
        }
        return res;
    }
    //get all the price including entrance and route fee
    public int getTotalPrice(){
        int res = getSinglePrice();
        for(Route route : returnRoutes){
            res += route.getAdultPrice();
        }
        res += castle.getAdultPrice();
        return res;
    }
    //save a journey as a ticket
    public Ticket toTicket(String username, String date, String time, String returnTime, int adultNum, int kidsNum){
        //use the currentTimeMillis as the ticket id to avoid repetition
        String ticketId = Long.toString(System.currentTimeMillis()+Long.parseLong(username));
        //calculate the price
        int adultPrice = castle.getAdultPrice();
        for(Route route : routes){
            adultPrice += route.getAdultPrice();
        }
        for(Route route : returnRoutes){
            adultPrice += route.getAdultPrice();
        }
        int kidsPrice = castle.getKidsPrice();
        for(Route route : routes){
            kidsPrice += route.getKidsPrice();
        }
        for(Route route : returnRoutes){
            kidsPrice += route.getKidsPrice();
        }
        int totalPrice = adultPrice * adultNum + kidsPrice * kidsNum;
        Ticket ticket = new Ticket(ticketId, castle.getName(), username, journeyId, date, time, returnTime, adultNum, kidsNum, totalPrice);
        return ticket;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public Castle getCastle() {
        return castle;
    }

    public void setCastle(Castle castle) {
        this.castle = castle;
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }

    public ArrayList<Route> getReturnRoutes() {
        return returnRoutes;
    }

    public void setReturnRoutes(ArrayList<Route> returnRoutes) {
        this.returnRoutes = returnRoutes;
    }

    @Override
    public String toString() {
        return "Journey{" +
                "journeyId='" + journeyId + '\'' +
                ", castle=" + castle +
                ", routes=" + routes +
                ", returnRoutes=" + returnRoutes +
                '}';
    }

}
