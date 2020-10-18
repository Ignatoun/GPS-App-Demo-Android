package com.example.gps_app_3.model;

import java.io.Serializable;

public class Location implements Serializable {

    private Long id;

    private String latitude;
    private String longitude;

    private Trip trip;

    public Location() {
    }

    public Location(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + this.id +
                ", latitude=" + this.latitude +
                ", longitude=" + this.longitude +
                "}";
    }
}
