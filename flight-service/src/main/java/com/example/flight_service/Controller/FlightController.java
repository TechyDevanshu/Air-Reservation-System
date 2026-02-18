package com.example.flight_service.Controller;

import com.example.flight_service.Dto.AddFlightRequest;
import com.example.flight_service.Entity.Flight;
import com.example.flight_service.Service.FlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @PostMapping("/admin/add")
    public Flight addFlight(@Valid @RequestBody AddFlightRequest request) {
        return flightService.addFlight(request);
    }

    @GetMapping("/admin/all")
    public List<Flight> getAllFlights() {
        return flightService.getAllflights();
    }

    @PutMapping("/admin/update/{flightId}")
    public Flight updateFlight(@PathVariable Long flightId, @Valid @RequestBody AddFlightRequest request) {
        return flightService.updateFlight(flightId, request);
    }

    @DeleteMapping("/admin/delete/{flightId}")
    public void deleteFlight(@PathVariable Long flightId){
        flightService.deleteFlight(flightId);
    }


    @GetMapping("/search")
    public List<Flight> searchFlights(@RequestParam String source,
                                      @RequestParam String destination,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return flightService.searchFlight(source, destination, date);

    }

    @GetMapping("/availability/{flightId}/{seats}")
    public boolean checkAvailability(@PathVariable Long flightId, @PathVariable int seats) {
        return flightService.checkAvailability(flightId, seats);
    }

    @PutMapping("/reduce/{flightId}/{seats}")
    public void reduceSeats(@PathVariable Long flightId, @PathVariable int seats) {
        flightService.reduceSeats(flightId, seats);
    }

    @PutMapping("/increase/{flightId}/{seats}")
    public void increaseSeats(@PathVariable Long flightId, @PathVariable int seats) {
        flightService.increaseSeats(flightId, seats);
    }

    @GetMapping("/{flightId}")
    public Flight getFlight(@PathVariable Long flightId) {
        return flightService.getFlightById(flightId);
    }
}
