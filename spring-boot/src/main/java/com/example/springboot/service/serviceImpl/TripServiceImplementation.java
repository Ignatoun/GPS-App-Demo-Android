package com.example.springboot.service.serviceImpl;

import com.example.springboot.model.Trip;
import com.example.springboot.repository.TripRepository;
import com.example.springboot.service.LocationService;
import com.example.springboot.service.TripService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TripServiceImplementation implements TripService {

    private static final Logger LOGGER = LogManager.getLogger(LocationService.class);

    @Autowired
    private TripRepository tripRepository;

    @Override
    public List<Trip> getAllTrips() {
        LOGGER.info("Service method called to view list of all Trips");
        Iterable<Trip> tripList = tripRepository.findAll();
        List<Trip> trips = new ArrayList<>();
        tripList.forEach(trips::add);
        LOGGER.info("Records found: " + trips.size());
        return trips;
    }

    @Override
    public Trip createTrip(Trip trip) {
        LOGGER.info("Service method called to create Trip: " + trip.toString());

        Trip trip1 = tripRepository.save(trip);

        return trip1;
    }
}
