package com.example.frontend_service.Controller;

import com.example.frontend_service.Dto.BookFlightRequest;
import com.example.frontend_service.Entity.Flight;
import com.example.frontend_service.Service.BookingClientService;
import com.example.frontend_service.Service.FlightClientService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final FlightClientService flightClientService;
    private final BookingClientService bookingClientService;

    /* ================================
       LOAD BOOKING PAGE
    ================================= */

    @GetMapping("/create/{flightId}")
    public String bookingPage(@PathVariable Long flightId,
                              HttpSession session,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        if (!isPassenger(session)) {
            return "redirect:/auth/login";
        }

        String token = (String) session.getAttribute("JWT");

        try {
            Flight flight = flightClientService.getFlightById(flightId, token);

            model.addAttribute("flight", flight);
            model.addAttribute("flightId", flightId);

            return "passenger/create-booking";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Unable to load flight details.");
            return "redirect:/flights/search";
        }
    }

    /* ================================
       CREATE BOOKING
    ================================= */
    @PostMapping("/create")
    public String createBooking(@RequestParam Long flightId,
                                @RequestParam int seats,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        String token = (String) session.getAttribute("JWT");
        Long userId = (Long) session.getAttribute("USER_ID");

        try {

            // Get flight to extract journey date
            Flight flight = flightClientService.getFlightById(flightId, token);

            BookFlightRequest request = new BookFlightRequest();
            request.setFlightId(flightId);
            request.setUserId(userId);
            request.setJourneyDate(flight.getJourneyDate());
            request.setSeatsBooked(seats);

            bookingClientService.createBooking(request, token);

            redirectAttributes.addFlashAttribute("success",
                    "Booking created successfully!");

            return "redirect:/bookings/my";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("error",
                    "Booking failed: " + e.getMessage());

            return "redirect:/bookings/create/" + flightId;
        }
    }

    /* ================================
       ROLE CHECK
    ================================= */

    private boolean isPassenger(HttpSession session) {

        if (session == null || session.getAttribute("JWT") == null)
            return false;

        String role = (String) session.getAttribute("ROLE");

        if (role == null)
            return false;

        return role.replace("ROLE_", "").equals("PASSENGER");
    }

    /* ================================
       SAFE ERROR MESSAGE
    ================================= */

    private String extractMessage(Exception e) {
        if (e.getMessage() == null)
            return "Something went wrong. Please try again.";
        return e.getMessage();
    }

    @GetMapping("/my")
    public String myBookings(HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (!isPassenger(session)) {
            return "redirect:/auth/login";
        }

        String token = (String) session.getAttribute("JWT");
        Long userId = (Long) session.getAttribute("USER_ID");

        try {

            var bookings = bookingClientService.getUserBookings(userId, token);

            model.addAttribute("bookings", bookings);

            return "passenger/my-bookings";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("error",
                    "Unable to fetch bookings.");

            return "redirect:/user/dashboard";
        }
    }

    @PostMapping("/cancel/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        if (!isPassenger(session)) {
            return "redirect:/auth/login";
        }

        String token = (String) session.getAttribute("JWT");

        try {

            bookingClientService.cancelBooking(bookingId, token);

            redirectAttributes.addFlashAttribute("success",
                    "Booking cancelled successfully.");

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("error",
                    "Unable to cancel booking.");
        }

        return "redirect:/bookings/my";
    }

}
