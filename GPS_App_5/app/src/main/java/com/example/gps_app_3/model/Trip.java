package com.example.gps_app_3.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Trip {

    private Long id;

    private String name;

    List<Location> locations = new ArrayList<Location>();

    private UserTrip userTrip;

    private String startDate;

    private String finishDate;

    public Trip(String name) {
        this.name = name;
    }

    public Trip() {
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Location> getLocations() {
        return locations;
    }

    @NonNull
    @Override
    public String toString() {
        return "Trip{" +
                "locations:" + getLocations() +
                "}";
    }
}
