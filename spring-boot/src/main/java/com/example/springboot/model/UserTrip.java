package com.example.springboot.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class UserTrip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn( name = "user_id", referencedColumnName = "id")
    List<Trip> trips = new ArrayList<Trip>();

    public UserTrip() {
    }

    public UserTrip(String name) {
        this.userName = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public List<Trip> getTrips() {
        return trips;
    }
}
