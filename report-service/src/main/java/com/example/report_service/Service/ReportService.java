package com.example.report_service.Service;

import com.example.report_service.Client.BookingClient;
import com.example.report_service.Client.FlightClient;
import com.example.report_service.Dto.BookingDTO;
import com.example.report_service.Dto.BookingReport;
import com.example.report_service.Dto.FlightDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final BookingClient bookingClient;
    private final FlightClient flightClient;

    public List<BookingReport> getReportByJourneyDate(LocalDate date) {

        List<BookingDTO> bookings = getAllBookingsSafe();

        return bookings.stream()
                .filter(b -> b.getJourneyDate().equals(date))
                .map(this::buildReportSafe)
                .toList();
    }

    public List<BookingReport> getReportByDestination(String destination) {

        List<BookingDTO> bookings = getAllBookingsSafe();

        return bookings.stream()
                .map(this::buildReportSafe)
                .filter(r -> r.getDestination().equalsIgnoreCase(destination))
                .toList();
    }

    @CircuitBreaker(name = "bookingServiceCB", fallbackMethod = "bookingServiceFallback")
    public List<BookingDTO> getAllBookingsSafe() {
        return bookingClient.getAllBookings();
    }

    @CircuitBreaker(name = "flightServiceCB", fallbackMethod = "flightServiceFallback")
    public FlightDTO getFlightSafe(Long flightId) throws Exception {
        return flightClient.getFlight(flightId);
    }


    private BookingReport buildReportSafe(BookingDTO booking) {

        FlightDTO flight;

        try {
            flight = getFlightSafe(booking.getFlightId());
        } catch (Exception e) {
            flight = flightServiceFallback(booking.getFlightId(), e);
        }

        return BookingReport.builder()
                .bookingId(booking.getBookingId())
                .flightNumber(flight.getFlightNumber())
                .airLine(flight.getAirline())
                .source(flight.getSource())
                .destination(flight.getDestination())
                .journeyDate(booking.getJourneyDate())
                .seatsBooked(booking.getSeatsBooked())
                .status(booking.getBookingStatus())
                .build();
    }

    public List<BookingDTO> bookingServiceFallback(Throwable ex) {
        return Collections.emptyList();
    }

    public FlightDTO flightServiceFallback(Long flightId, Throwable ex) {

        System.out.println("⚠ Flight service failed for id: " + flightId);
        System.out.println("Reason: " + ex.getMessage());

        FlightDTO dto = new FlightDTO();
        dto.setFlightNumber("N/A");
        dto.setAirline("Unavailable");
        dto.setSource("Unknown");
        dto.setDestination("Unknown");

        return dto;
    }

}
