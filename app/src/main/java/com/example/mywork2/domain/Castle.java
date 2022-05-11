package com.example.mywork2.domain;

import java.util.ArrayList;

/**
 * @author Jing
 * function: used for store a temp castle object in the program
 */
public class Castle {
    private String name;
    private String postcode;
    private int price;
//    private int adultPrice;
//    private int kidsPrice;
    private ArrayList<NearbyPOI> nearbyPOIs;

    public Castle(String name, String postcode, int price, ArrayList<NearbyPOI> nearbyPOIs) {
        this.name = name;
        this.postcode = postcode;
        this.price = price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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
                ", price=" + price +
                ", nearbyPOIs=" + nearbyPOIs +
                '}';
    }
}
