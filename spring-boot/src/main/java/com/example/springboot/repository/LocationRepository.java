package com.example.springboot.repository;

import com.example.springboot.model.Location;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Long> {

}
