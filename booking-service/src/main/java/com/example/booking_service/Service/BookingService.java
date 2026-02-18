package com.example.booking_service.Service;

import com.example.booking_service.Client.FlightClient;
import com.example.booking_service.Dto.BookFlightRequest;
import com.example.booking_service.Dto.FlightDTO;
import com.example.booking_service.Entity.Booking;
import com.example.booking_service.Entity.BookingStatus;
import com.example.booking_service.Exception.BusinessException;
import com.example.booking_service.Exception.ResourceNotFoundException;
import com.example.booking_service.Repository.BookingRepo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepo bookingRepo;

    private final FlightClient flightClient;

    public BookingService(FlightClient flightClient) {
        this.flightClient = flightClient;
    }

    //Book Flight
    public Booking bookFlight(BookFlightRequest request) {

        // Fetch flight details;
        FlightDTO flight = getFlightSafe(request.getFlightId());

        // Validate journey date
        if (!flight.getJourneyDate().equals(request.getJourneyDate())) {
            throw new BusinessException(
                    "Flight is not available for the selected journey date");
        }

        //  Check seat availability
        if (flight.getAvailableSeats() < request.getSeatsBooked()) {
            throw new BusinessException("Not enough seats available");
        }

        // Calculate price (BACKEND ONLY)
        double totalPrice = flight.getPrice() * request.getSeatsBooked();

        // Reduce seats
        flightClient.reduceSeats(request.getFlightId(), request.getSeatsBooked());

        // Save booking
        Booking booking = new Booking();
        booking.setUserId(request.getUserId());
        booking.setFlightId(request.getFlightId());
        booking.setJourneyDate(request.getJourneyDate());
        booking.setSeatsBooked(request.getSeatsBooked());
        booking.setTotalPrice(totalPrice);
        booking.setBookingStatus(BookingStatus.CONFIRMED);

        return bookingRepo.save(booking);
    }

    @CircuitBreaker(name = "flightServiceFallback", fallbackMethod = "getFlightFallback")
    public FlightDTO getFlightSafe(Long flightId) {
        return flightClient.getFlight(flightId);
    }

    public FlightDTO getFlightFallback(Long flightId, Throwable ex) {
        throw new BusinessException(
                "Flight service is currently unavailable. Please try again later."
        );
    }

    //View booking status
    public Booking getBooking(Long bookingId) {
        return bookingRepo.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + bookingId));
    }

    //Cancel Booking
    @CircuitBreaker(name = "flightServiceCB", fallbackMethod = "cancelBookingFallback")
    public String cancelBooking(Long bookingId) {
        Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + bookingId));

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new BusinessException("Booking already cancelled");
        }
        flightClient.increaseSeats(booking.getFlightId(), booking.getSeatsBooked());

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepo.save(booking);

        return "Booking cancelled successfully";
    }

    //Fallback for cancelBooking
    public String cancelBookingFallback(Long bookingId, Throwable ex) {
        return "Unable to cancel booking right now. Flight Service is unavailable.";
    }

    //View all bookings of user;
    public List<Booking> getUserBooking(Long userId) {
        return bookingRepo.findByUserId(userId);
    }
}
