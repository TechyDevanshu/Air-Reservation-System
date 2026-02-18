package com.example.report_service.Client;

import com.example.report_service.Dto.FlightDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "flight-service", url = "http://localhost:9002")
public interface FlightClient {

    @GetMapping("/flights/{flightId}")
    FlightDTO getFlight(@PathVariable Long flightId);
}
