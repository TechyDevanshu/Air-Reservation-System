package com.example.frontend_service.Dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ReportDTO {

    private Long bookingId;
    private String airLine;
    private String flightNumber;
    private String source;
    private String destination;
    private LocalDate journeyDate;
    private int seatsBooked;
    private String status;
}
