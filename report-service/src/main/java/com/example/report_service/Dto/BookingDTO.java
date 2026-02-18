package com.example.report_service.Dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingDTO {
    private Long bookingId;

    private Long userId;
    private Long flightId;

    private LocalDate journeyDate;
    private int seatsBooked;
    private double totalPrice;
    private String bookingStatus;
}
