package com.example.report_service.Dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FlightDTO {
    private Long id;


    private String flightNumber;
    private String airline;
    private String source;
    private String destination;
    private LocalDate journeyDate;
}
