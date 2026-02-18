package com.example.report_service.Client;

import com.example.report_service.Dto.BookingDTO;
import com.example.report_service.factory.BookingClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "booking-service", url = "http://localhost:9003", fallbackFactory = BookingClientFallbackFactory.class)
public interface BookingClient {

    @GetMapping("/bookings/all")
    List<BookingDTO>getAllBookings();
}