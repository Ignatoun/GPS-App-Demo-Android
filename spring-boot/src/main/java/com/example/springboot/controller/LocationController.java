package com.example.springboot.controller;

import com.example.springboot.model.Location;
import com.example.springboot.repository.LocationRepository;
import com.example.springboot.service.LocationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path="/demo")
public class LocationController {

    private static final Logger LOGGER = LogManager.getLogger(LocationController.class);

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationRepository locationRepository;

    @PostMapping(path="/addLocation")
    public ResponseEntity<Location> addLocation(@RequestBody Location location) {
        LOGGER.info("Controller method called to create Location;" +
                " location="+ location.toString());

        return ResponseEntity.ok().body(this.locationService.createLocation(location));
    }

    @GetMapping(path="/allLocations")
    public ResponseEntity<List<Location>> getLocations() {
        LOGGER.info("Controller method called to view all list of Users");
        return ResponseEntity.ok().body(locationService.getAllLocations());
    }
}
