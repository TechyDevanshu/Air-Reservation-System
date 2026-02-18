package com.example.frontend_service.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String flightNumber;
    private String airline;
    private String source;
    private String destination;
    private LocalDate journeyDate;

    private int totalSeats;
    private Integer availableSeats;

    private double price;

}
