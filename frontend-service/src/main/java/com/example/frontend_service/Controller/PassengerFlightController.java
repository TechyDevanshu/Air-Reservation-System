package com.example.frontend_service.Controller;

import com.example.frontend_service.Entity.Flight;
import com.example.frontend_service.Service.FlightClientService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/flights")
@RequiredArgsConstructor
public class PassengerFlightController {

    private final FlightClientService flightClientService;

    @GetMapping("/search")
    public String searchFlights(
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            HttpSession session,
            Model model) {

        // Show empty page first time
        if (source == null || destination == null || date == null) {
            return "passenger/search-flights";
        }

        // Validate past date
        if (date.isBefore(LocalDate.now())) {
            model.addAttribute("error", "Journey date cannot be in the past.");
            return "passenger/search-flights";
        }

        try {
            String token = (String) session.getAttribute("JWT");

            List<Flight> flights =
                    flightClientService.searchFlights(source, destination, date, token);

            model.addAttribute("flights", flights);

        } catch (Exception e) {
            model.addAttribute("error", "No flights found for selected route/date.");
        }

        return "passenger/search-flights";
    }
}
