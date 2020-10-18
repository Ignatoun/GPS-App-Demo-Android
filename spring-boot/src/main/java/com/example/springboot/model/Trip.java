package com.example.springboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn( name = "trip_id", referencedColumnName = "id")
    List<Location> locations = new ArrayList<Location>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserTrip userTrip;

    private String startDate;

    private String finishDate;

    public Trip(String name) {
        this.name = name;
    }

    public Trip() {
    }

    public UserTrip getUserTrip() {
        return userTrip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }
}
