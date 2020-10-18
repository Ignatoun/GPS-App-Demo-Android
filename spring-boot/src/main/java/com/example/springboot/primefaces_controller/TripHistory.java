package com.example.springboot.primefaces_controller;

import com.example.springboot.model.Trip;
import com.example.springboot.service.serviceImpl.TripServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import java.util.List;

@Named
public class TripHistory {

    private List<Trip> tripList;

    @Autowired
    TripServiceImplementation tripServiceImplementation;

    @PostConstruct
    public void setup() {
        tripList = tripServiceImplementation.getAllTrips();
    }

    public List<Trip> getTripList() {
        return tripList;
    }

    public void setTripList(List<Trip> tripList) {
        this.tripList = tripList;
    }
}
