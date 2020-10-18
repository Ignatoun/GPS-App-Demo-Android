package com.example.springboot.service;

import com.example.springboot.model.Location;

import java.util.List;

public interface LocationService {

    List<Location> getAllLocations();

    Location createLocation(Location location);
}
