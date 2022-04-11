package com.example.mywork2.domain;

import java.io.Serializable;

public class NearbyPOI implements Serializable {
    private String castleName;
    private String poiName;
    private String category;
    private float rating;
    private String postcode;

    public NearbyPOI(String castleName, String poiName, String category, float rating, String postcode) {
        this.castleName = castleName;
        this.poiName = poiName;
        this.category = category;
        this.rating = rating;
        this.postcode = postcode;
    }

    public String getCastleName() {
        return castleName;
    }

    public void setCastleName(String castleName) {
        this.castleName = castleName;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Override
    public String toString() {
        return "NearbyPOI{" +
                "castleName='" + castleName + '\'' +
                ", poiName='" + poiName + '\'' +
                ", category='" + category + '\'' +
                ", rating=" + rating +
                ", postcode='" + postcode + '\'' +
                '}';
    }
}
