package com.example.springboot.service.serviceImpl;

import com.example.springboot.controller.excel_generator.ExcelGenerator;
import com.example.springboot.model.Trip;
import com.example.springboot.model.UserTrip;
import com.example.springboot.repository.TripRepository;
import com.example.springboot.repository.UserTripRepository;
import com.example.springboot.service.LocationService;
import com.example.springboot.service.UserTripService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserTripServiceImplementation implements UserTripService {

    private static final Logger LOGGER = LogManager.getLogger(LocationService.class);

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserTripRepository userTripRepository;

    @Override
    public List<UserTrip> getAllUserTrips() {
        LOGGER.info("Service method called to view list of all UsertTrips");
        Iterable<UserTrip> userTripList = userTripRepository.findAll();
        List<UserTrip> userTrips = new ArrayList<>();
        userTripList.forEach(userTrips::add);
        LOGGER.info("Records found: " + userTrips.size());
        return userTrips;
    }

    @Override
    public UserTrip createNewUserTrip(UserTrip userTrip) {
        LOGGER.info("Service method called to create UserTrip: " + userTrip.toString());

        UserTrip userTrip1 = userTripRepository.save(userTrip);

        return userTrip1;
    }

    @Override
    public UserTrip getUserById(Integer userId) {
        LOGGER.info("Service method called to view UserTrip by id="+userId);
        Optional<UserTrip> userTripOptional = userTripRepository.findById(Long.valueOf(userId));
        if(userTripOptional.isPresent()) {
            LOGGER.info("User found: " +userTripOptional.get().toString());
            return userTripOptional.get();
        } else {
            LOGGER.info("User not found");
            LOGGER.error("User not found");
            return null;
        }
    }

    @Override
    public UserTrip getUserByName(String userName) {
        LOGGER.info("Service method called to view User by Name="+userName);
        UserTrip userTrip = userTripRepository.findByUsername(userName);
        if(userTrip!=null) {
            LOGGER.info("User found: " + userTrip.toString());
            return userTrip;
        } else {
            LOGGER.info("User not found");
            LOGGER.error("User not found");
            return null;
        }
    }

    @Override
    public UserTrip updateUser(UserTrip userTrip) {
        LOGGER.info("Service method called to update User;" +
                " user="+userTrip.toString());
        Optional <UserTrip> userDb = userTripRepository.findById(userTrip.getId());

        if(userDb.isPresent()) {
            LOGGER.info("User found");
            UserTrip userTripUpdate = userDb.get();

            userTripUpdate.getTrips().add(userTrip.getTrips().get(
                    userTrip.getTrips().size() - 1
            ));
            return userTripRepository.save(userTripUpdate);
        } else {
            LOGGER.info("User not found");
            LOGGER.error("User not found");
            return null;
        }
    }

    @Override
    public InputStreamResource getExcelTripHistory(String userName) throws IOException {
        LOGGER.info("Service method called to download "
                + userName + "'s trip history");

        UserTrip userTrip = userTripRepository.findByUsername(userName);

        List<Trip> trips = userTrip.getTrips();

        ByteArrayInputStream in = ExcelGenerator.tripsToExcel(trips);
        return new InputStreamResource(in);
    }
}
