package com.example.booking_service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    private Long userId;
    private Long flightId;

    private LocalDate journeyDate;
    private int seatsBooked;
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

}
