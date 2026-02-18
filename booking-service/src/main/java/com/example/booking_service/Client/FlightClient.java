package com.example.booking_service.Client;

import com.example.booking_service.Dto.FlightDTO;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "flight-service")
public interface FlightClient {

    @GetMapping("/flights/{flightId}")
    FlightDTO getFlight(@PathVariable Long flightId);

    @GetMapping("/flights/availability/{flightId}/{seats}")
    boolean checkAvailability(
            @PathVariable Long flightId,
            @PathVariable int seats
    );

    @PutMapping("/flights/reduce/{flightId}/{seats}")
    void reduceSeats(@PathVariable Long flightId, @PathVariable int seats);

    @PutMapping("/flights/increase/{flightId}/{seats}")
    void increaseSeats(@PathVariable Long flightId, @PathVariable int seats);

}
