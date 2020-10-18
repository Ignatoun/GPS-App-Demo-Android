package com.example.springboot.service.serviceImpl;

import com.example.springboot.model.Location;
import com.example.springboot.repository.LocationRepository;
import com.example.springboot.repository.TripRepository;
import com.example.springboot.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LocationServiceImplementation implements LocationService {

    private static final Logger LOGGER = LogManager.getLogger(LocationService.class);

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TripRepository tripRepository;

    @Override
    public List<Location> getAllLocations() {
        LOGGER.info("Service method called to view list of all Locations");
        Iterable<Location> locationList = locationRepository.findAll();
        List<Location> locations = new ArrayList<>();
        locationList.forEach(locations::add);
        LOGGER.info("Records found: " + locations.size());
        return locations;
    }

    @Override
    public Location createLocation(Location location) {
        LOGGER.info("Service method called to create Location: " + location.toString());

        Location location1 = locationRepository.save(location);

        return location1;
    }
}
