package com.example.frontend_service.Service;

import com.example.frontend_service.Dto.ReportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportClientService {

    private final WebClient webClient;

    public List<ReportDTO> getByDestination(String destination, String token) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/reports/destination")
                        .queryParam("destination", destination)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToFlux(ReportDTO.class)   // ✅ FIXED
                .collectList()
                .block();
    }

    public List<ReportDTO> getByDate(String date, String token) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/reports/date")
                        .queryParam("date", date)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToFlux(ReportDTO.class)   // ✅ FIXED
                .collectList()
                .block();
    }
}