package com.example.springboot.controller;

import com.example.springboot.model.Trip;
import com.example.springboot.service.TripService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path="/demo")
public class TripController {

    private static final Logger LOGGER = LogManager.getLogger(LocationController.class);

    @Autowired
    TripService tripService;

    @PostMapping(path="/addTrip")
    public ResponseEntity<Trip> addUserTrip(@RequestBody Trip trip) {
        LOGGER.info("Controller method called to create Trip;" +
                " trip="+ trip.toString());

        return ResponseEntity.ok().body(this.tripService.createTrip(trip));
    }

    @GetMapping(path="/allTrips")
    public ResponseEntity<List<Trip>> getTrips() {
        LOGGER.info("Controller method called to view all list of Trips");
        return ResponseEntity.ok().body(tripService.getAllTrips());
    }
}
