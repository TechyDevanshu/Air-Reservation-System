package com.example.flight_service.Repository;

import com.example.flight_service.Entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FlightRepo extends JpaRepository<Flight, Long> {

    List<Flight> findBySourceAndDestinationAndJourneyDate(
            String source, String destination, LocalDate date
    );
}
