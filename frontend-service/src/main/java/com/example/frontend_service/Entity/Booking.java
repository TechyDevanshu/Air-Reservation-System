package com.example.frontend_service.Entity;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Booking {

    private Long bookingId;
    private Long userId;
    private Long flightId;
    private LocalDate journeyDate;
    private int seatsBooked;
    private double totalPrice;
    private BookingStatus bookingStatus;
}
