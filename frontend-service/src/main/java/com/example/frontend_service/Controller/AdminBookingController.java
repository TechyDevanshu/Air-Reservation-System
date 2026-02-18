package com.example.frontend_service.Controller;

import com.example.frontend_service.Entity.Booking;
import com.example.frontend_service.Service.BookingClientService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/bookings")
@RequiredArgsConstructor
public class AdminBookingController {

    private final BookingClientService bookingClientService;

    @GetMapping
    public String viewAllBookings(HttpSession session, Model model) {

        String role = (String) session.getAttribute("ROLE");

        if (role == null || !role.equals("ROLE_ADMIN")) {
            return "redirect:/auth/login";
        }

        String token = (String) session.getAttribute("JWT");

        List<Booking> bookings =
                bookingClientService.getAllBookings(token);

        // ✅ Calculate total revenue in backend
        double totalRevenue = bookings.stream()
                .filter(b -> b.getBookingStatus().name().equals("CONFIRMED"))
                .mapToDouble(Booking::getTotalPrice)
                .sum();

        model.addAttribute("bookings", bookings);
        model.addAttribute("totalRevenue", totalRevenue);

        return "admin/view-bookings";
    }

}
