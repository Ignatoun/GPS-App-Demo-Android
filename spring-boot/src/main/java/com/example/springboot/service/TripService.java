package com.example.springboot.service;

import com.example.springboot.model.Trip;

import java.util.List;

public interface TripService {

    List<Trip> getAllTrips();

    Trip createTrip(Trip trip);
}
