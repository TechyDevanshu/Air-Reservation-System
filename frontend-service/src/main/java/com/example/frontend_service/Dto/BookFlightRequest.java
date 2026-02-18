package com.example.frontend_service.Dto;

import jakarta.validation.constraints.*;
import lombok.Data;


import java.time.LocalDate;

@Data
public class BookFlightRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Flight ID is required")
    private Long flightId;

    @NotNull(message = "Journey date is required")
    @FutureOrPresent(message = "Journey date cannot be in the past")
    private LocalDate journeyDate;

    @Min(value = 1, message = "Seats booked must be at least 1")
    private int seatsBooked;
}
