package com.example.frontend_service.Service;

import com.example.frontend_service.Dto.AddFlightRequest;
import com.example.frontend_service.Entity.Flight;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightClientService {

    private final WebClient webClient;

    public List<Flight> getAllFlights(String token) {
        return webClient.get()
                .uri("/flights/admin/all")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToFlux(Flight.class)
                .collectList()
                .block();
    }

    public Flight addFlight(AddFlightRequest request, String token) {
        return webClient.post()
                .uri("/flights/admin/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.isError(), response ->
                        response.bodyToMono(String.class)
                                .map(errorBody -> new RuntimeException(errorBody))
                )
                .bodyToMono(Flight.class)
                .block();
    }


    public Flight updateFlight(Long id, AddFlightRequest request, String token) {
        return webClient.put()
                .uri("/flights/admin/update/" + id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.isError(), response ->
                        response.bodyToMono(String.class)
                                .map(errorBody -> new RuntimeException(errorBody))
                )
                .bodyToMono(Flight.class)
                .block();
    }


    public void deleteFlight(Long id, String token) {
        webClient.delete()
                .uri("/flights/admin/delete/" + id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public Flight getFlightById(Long id, String token) {
        return webClient.get()
                .uri("/flights/" + id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Flight.class)
                .block();
    }

    public List<Flight> searchFlights(String source,
                                      String destination,
                                      LocalDate date,
                                      String token) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/flights/search")
                        .queryParam("source", source)
                        .queryParam("destination", destination)
                        .queryParam("date", date)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToFlux(Flight.class)
                .collectList()
                .block();
    }

    public boolean checkAvailability(Long flightId, int seats, String token) {
        return Boolean.TRUE.equals(webClient.get()
                .uri("/flights/availability/" + flightId + "/" + seats)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block());
    }

    public void reduceSeats(Long flightId, int seats, String token) {
        webClient.put()
                .uri("/flights/reduce/" + flightId + "/" + seats)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }




}
