package com.example.booking_service.Controller;

import com.example.booking_service.Dto.BookFlightRequest;
import com.example.booking_service.Entity.Booking;
import com.example.booking_service.Entity.BookingStatus;
import com.example.booking_service.Repository.BookingRepo;
import com.example.booking_service.Service.BookingService;
import com.netflix.discovery.converters.Auto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepo bookingRepo;

    @PostMapping("/book")
    public Booking bookFlight(@Valid @RequestBody BookFlightRequest request) {
        return bookingService.bookFlight(request);
    }

    @GetMapping("/{bookingId}")
    public Booking getBooking(@PathVariable Long bookingId) {
        return bookingService.getBooking(bookingId);
    }

    @PutMapping("/cancel/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId) {
        return bookingService.cancelBooking(bookingId);
    }

    @GetMapping("/user/{userId}")
    public List<Booking> getUserBookings(@PathVariable Long userId) {
        return bookingService.getUserBooking(userId);
    }

    @GetMapping("/all")
    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }
}
