package com.example.springboot.controller;

import com.example.springboot.controller.excel_generator.ExcelGenerator;
import com.example.springboot.model.Location;
import com.example.springboot.model.Trip;
import com.example.springboot.model.UserTrip;
import com.example.springboot.repository.UserTripRepository;
import com.example.springboot.service.UserTripService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(path="/demo")
public class UserTripController {

    private static final Logger LOGGER = LogManager.getLogger(LocationController.class);

    @Autowired
    UserTripService userTripService;

    @PostMapping(path="/allUserTrips")
    public ResponseEntity<UserTrip> addUserTrip(@RequestBody UserTrip userTrip) {
        LOGGER.info("Controller method called to create UserTrip;" +
                " userTrip="+ userTrip.toString());

        return ResponseEntity.ok().body(this.userTripService.createNewUserTrip(userTrip));
    }

    @GetMapping(path="/allUserTrips")
    public ResponseEntity<List<UserTrip>> getUserTrips() {
        LOGGER.info("Controller method called to view all list of UserTrips");
        return ResponseEntity.ok().body(userTripService.getAllUserTrips());
    }

    @PutMapping(path="/allUserTrips/{id}")
    public ResponseEntity<UserTrip> updateUser(@PathVariable Long id, @RequestBody UserTrip userTrip) {
        userTrip.setId(id);
        LOGGER.info("Controller method called to update User;" +
                " id="+id+", user="+userTrip.toString());

        return ResponseEntity.ok().body(this.userTripService.updateUser(userTrip));
    }

    @GetMapping("/allUserTrips/{userName}")
    public ResponseEntity <UserTrip> getUserByName(@PathVariable String userName) {
        LOGGER.info("Controller method called to view User by name="+userName);
        return ResponseEntity.ok().body(userTripService.getUserByName(userName));
    }

    @GetMapping("/allUserTrips/{userName}/trips.xlsx")
    public ResponseEntity<InputStreamResource> getExcelTripHistory(
            @PathVariable String userName) throws IOException {
        LOGGER.info("Controller method called to download "
                + userName + "'s trip history");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" +
                userName + "Trips.xlsx");

        return ResponseEntity.ok().headers(headers).body(userTripService.getExcelTripHistory(userName));
    }

    @GetMapping("/allUserTrips/tripHistory")
    public String getUserPageTripHistory() {
        return "trip_history";
    }

}
