package com.example.frontend_service.Controller;

import com.example.frontend_service.Dto.AddFlightRequest;
import com.example.frontend_service.Entity.Flight;
import com.example.frontend_service.Service.FlightClientService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/flights/admin")
@RequiredArgsConstructor
public class AdminFlightController {

    private final FlightClientService flightClientService;

    @GetMapping
    public String manageFlights(HttpSession session, Model model) {

        if (!isAdmin(session))
            return "redirect:/access-denied";

        String token = (String) session.getAttribute("JWT");

        model.addAttribute("flights",
                flightClientService.getAllFlights(token));

        return "admin/manage-flights";
    }

    @GetMapping("/add")
    public String addFlightPage(HttpSession session, Model model) {

        if (!isAdmin(session))
            return "redirect:/access-denied";

        model.addAttribute("flightRequest", new AddFlightRequest());
        model.addAttribute("mode", "add");

        return "admin/flight-form";
    }

    @PostMapping("/save")
    public String saveFlight(@ModelAttribute AddFlightRequest request,
                             @RequestParam(required = false) Long id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        if (!isAdmin(session))
            return "redirect:/access-denied";

        String token = (String) session.getAttribute("JWT");

        try {
            if (id == null) {
                flightClientService.addFlight(request, token);
                redirectAttributes.addFlashAttribute("success",
                        "Flight added successfully!");
            } else {
                flightClientService.updateFlight(id, request, token);
                redirectAttributes.addFlashAttribute("success",
                        "Flight updated successfully!");
            }
        } catch (Exception e) {

            String message = e.getMessage();

            // Clean backend JSON message if present
            if (message != null && message.contains("message")) {
                message = message.replaceAll(".*\"message\":\"", "")
                        .replaceAll("\".*", "");
            }
            redirectAttributes.addFlashAttribute("error", message);
        }

        return "redirect:/flights/admin";
    }

    @GetMapping("/edit/{id}")
    public String editFlight(@PathVariable Long id,
                             HttpSession session,
                             Model model) {

        if (!isAdmin(session))
            return "redirect:/access-denied";

        String token = (String) session.getAttribute("JWT");

        Flight flight = flightClientService.getFlightById(id, token);

        AddFlightRequest request = new AddFlightRequest();
        request.setFlightNumber(flight.getFlightNumber());
        request.setAirline(flight.getAirline());
        request.setSource(flight.getSource());
        request.setDestination(flight.getDestination());
        request.setJourneyDate(flight.getJourneyDate());
        request.setTotalSeats(flight.getTotalSeats());
        request.setAvailableSeats(flight.getAvailableSeats());
        request.setPrice(flight.getPrice());

        model.addAttribute("flightRequest", request);
        model.addAttribute("id", id);
        model.addAttribute("mode", "edit");

        return "admin/flight-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteFlight(@PathVariable Long id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        if (!isAdmin(session))
            return "redirect:/access-denied";

        String token = (String) session.getAttribute("JWT");

        try {
            flightClientService.deleteFlight(id, token);
            redirectAttributes.addFlashAttribute("success",
                    "Flight deleted successfully!");
        } catch (Exception e) {

            String message = e.getMessage();

            // Clean backend JSON message if present
            if (message != null && message.contains("message")) {
                message = message.replaceAll(".*\"message\":\"", "")
                        .replaceAll("\".*", "");
            }

            redirectAttributes.addFlashAttribute("error", message);
        }


        return "redirect:/flights/admin";
    }

    private boolean isAdmin(HttpSession session) {
        if (session == null || session.getAttribute("JWT") == null)
            return false;

        String role = (String) session.getAttribute("ROLE");
        if (role == null)
            return false;

        return role.replace("ROLE_", "").equals("ADMIN");
    }
}
