package com.example.mywork2.domain;

import java.util.ArrayList;

public class Castle {
    private String name;
    private String postcode;
    private int adultPrice;
    private int kidsPrice;
    private ArrayList<NearbyPOI> nearbyPOIs;

    public Castle(String name, String postcode, int adultPrice, int kidsPrice, ArrayList<NearbyPOI> nearbyPOIs) {
        this.name = name;
        this.postcode = postcode;
        this.adultPrice = adultPrice;
        this.kidsPrice = kidsPrice;
        this.nearbyPOIs = nearbyPOIs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
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

    public ArrayList<NearbyPOI> getNearbyPOIs() {
        return nearbyPOIs;
    }

    public void setNearbyPOIs(ArrayList<NearbyPOI> nearbyPOIs) {
        this.nearbyPOIs = nearbyPOIs;
    }

    @Override
    public String toString() {
        return "Castle{" +
                "name='" + name + '\'' +
                ", postcode='" + postcode + '\'' +
                ", adultPrice=" + adultPrice +
                ", kidsPrice=" + kidsPrice +
                ", nearbyPOIs=" + nearbyPOIs +
                '}';
    }
}
