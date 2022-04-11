package com.example.mywork2.domain;

public class Route {
    private String routeId;
    private String journeyId;
    private Transport transport;
    private String start;
    private String stop;
    private int duration;
    private int adultPrice;
    private int kidsPrice;
    private int led;

    public Route(String routeId, String journeyId, Transport transport, String start, String stop, int duration, int adultPrice, int kidsPrice, int led) {
        this.routeId = routeId;
        this.journeyId = journeyId;
        this.transport = transport;
        this.start = start;
        this.stop = stop;
        this.duration = duration;
        this.adultPrice = adultPrice;
        this.kidsPrice = kidsPrice;
        this.led = led;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAdultPrice() {
        return adultPrice;
    }

    public void setAdultPrice(int adultPrice) {
        this.adultPrice = adultPrice;
    }

    public int getKidsPrice() {
        return kidsPrice;
    }

    public void setKidsPrice(int kidsPrice) {
        this.kidsPrice = kidsPrice;
    }

    public int getLed() {
        return led;
    }

    public void setLed(int led) {
        this.led = led;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    @Override
    public String toString() {
        return "Route{" +
                "journeyId='" + journeyId + '\'' +
                ", transport=" + transport +
                ", start='" + start + '\'' +
                ", stop='" + stop + '\'' +
                ", duration=" + duration +
                ", adultPrice=" + adultPrice +
                ", kidsPrice=" + kidsPrice +
                ", led=" + led +
                '}';
    }
}
