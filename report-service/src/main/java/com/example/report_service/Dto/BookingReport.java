package com.example.report_service.Dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Data
@Builder
public class BookingReport {

    private Long bookingId;
    private String flightNumber;
    private String airLine;
    private String source;
    private String destination;
    private LocalDate journeyDate;
    private int seatsBooked;
    private String status;
}
