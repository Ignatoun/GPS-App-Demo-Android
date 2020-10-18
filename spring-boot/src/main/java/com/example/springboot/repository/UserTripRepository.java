package com.example.springboot.repository;

import com.example.springboot.model.UserTrip;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserTripRepository extends CrudRepository<UserTrip, Long> {

    @Query(value="SELECT * FROM User WHERE user_name= :userName", nativeQuery = true)
    UserTrip findByUsername(@Param("userName") String userName);
}
