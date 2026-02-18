package com.example.frontend_service.Service;

import com.example.frontend_service.Dto.BookFlightRequest;
import com.example.frontend_service.Entity.Booking;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingClientService {

    private final WebClient webClient;

    public Booking createBooking(BookFlightRequest request, String token) {

        return webClient.post()
                .uri("/bookings/book")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Booking.class)
                .block();
    }

    public @Nullable List<Booking> getUserBookings(Long userId, String token) {

        return webClient.get()
                .uri("/bookings/user/{userId}", userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToFlux(Booking.class)
                .collectList()
                .block();
    }

    public String cancelBooking(Long bookingId, String token) {

        return webClient.put()
                .uri("/bookings/cancel/" + bookingId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public List<Booking> getAllBookings(String token) {

        return webClient.get()
                .uri("/bookings/all")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToFlux(Booking.class)
                .collectList()
                .block();
    }


}

