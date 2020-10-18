package com.example.springboot.service;

import com.example.springboot.model.UserTrip;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.util.List;

public interface UserTripService {

    List<UserTrip> getAllUserTrips();

    UserTrip createNewUserTrip(UserTrip userTrip);

    UserTrip getUserById(Integer userId);

    UserTrip updateUser(UserTrip userTrip);

    UserTrip getUserByName(String userName);

    InputStreamResource getExcelTripHistory(String userName) throws IOException;
}
