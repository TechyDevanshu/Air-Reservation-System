package com.example.flight_service.Service;

import com.example.flight_service.Dto.AddFlightRequest;
import com.example.flight_service.Entity.Flight;
import com.example.flight_service.Exception.BusinessException;
import com.example.flight_service.Exception.ResourceNotFoundException;
import com.example.flight_service.Repository.FlightRepo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.List;

@Service
public class FlightService {

    @Autowired
    private FlightRepo flightRepo;

    //Admin - Add a new flight;
    public Flight addFlight(AddFlightRequest request) {
        if (request.getAvailableSeats() > request.getTotalSeats()) {
            throw new BusinessException("Available seats cannot exceed total seats");
        }

        Flight flight = new Flight();
        flight.setFlightNumber(request.getFlightNumber());
        flight.setAirline(request.getAirline());
        flight.setSource(request.getSource());
        flight.setDestination(request.getDestination());
        flight.setJourneyDate(request.getJourneyDate());
        flight.setTotalSeats(request.getTotalSeats());
        flight.setAvailableSeats(request.getAvailableSeats());
        flight.setPrice(request.getPrice());

        return flightRepo.save(flight);
    }

    //Admin - Update Flight;
    public Flight updateFlight(Long flightId, AddFlightRequest request) {

        Flight flight = flightRepo.findById(flightId).orElseThrow(() ->
                new ResourceNotFoundException("Flight not found with id " + flightId));

        if (request.getAvailableSeats() > request.getTotalSeats()) {
            throw new BusinessException("Available seats cannot exceed total seats");
        }

        flight.setFlightNumber(request.getFlightNumber());
        flight.setAirline(request.getAirline());
        flight.setSource(request.getSource());
        flight.setDestination(request.getDestination());
        flight.setJourneyDate(request.getJourneyDate());
        flight.setTotalSeats(request.getTotalSeats());
        flight.setAvailableSeats(request.getAvailableSeats());
        flight.setPrice(request.getPrice());

        return flightRepo.save(flight);
    }

    //Admin - Delete flight;
    public void deleteFlight(Long flightId) {
        Flight flight = flightRepo.findById(flightId).orElseThrow(() ->
                new ResourceNotFoundException("Flight not found with id " + flightId));

        flightRepo.delete(flight);
    }

    //Passenger - search flights
    public List<Flight> searchFlight(String source, String destination, LocalDate date) {
        List<Flight> flights = flightRepo.findBySourceAndDestinationAndJourneyDate(source, destination, date);

        if (flights.isEmpty()) {
            throw new ResourceNotFoundException("No flight found for given criteria");
        }
        return flights;
    }

    //Check seat availability
    public boolean checkAvailability(Long flightId, int seatsRequired) {
        Flight flight = flightRepo.findById(flightId).orElseThrow(() -> new ResourceNotFoundException("Flight not found with id " + flightId));

        return flight.getAvailableSeats() >= seatsRequired;
    }

    //Update available seats (used after booking)
    public void reduceSeats(Long flightId, int seatsBooked) {
        Flight flight = flightRepo.findById(flightId).orElseThrow(() -> new ResourceNotFoundException("Flight not found with id " + flightId));
        if (seatsBooked <= 0) {
            throw new BusinessException("Seats booked must be greater than zero");
        }

        if (flight.getAvailableSeats() < seatsBooked) {
            throw new BusinessException("Not enough seats available");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - seatsBooked);
        flightRepo.save(flight);
    }

    //Used when booking is canceled
    public void increaseSeats(Long flightId, int seats) {
        Flight flight = flightRepo.findById(flightId).orElseThrow(() -> new ResourceNotFoundException("Flight not found with id " + flightId));

        if (seats <= 0) {
            throw new BusinessException("Seats must be greater than zero");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() + seats);
        flightRepo.save(flight);
    }

    //Admin - view all flights
    public List<Flight> getAllflights() {
        return flightRepo.findAll();
    }

    public Flight getFlightById(Long flightId) {
        return flightRepo.findById(flightId).orElseThrow(() -> new ResourceNotFoundException("Flight not found with id " + flightId));
    }
}
