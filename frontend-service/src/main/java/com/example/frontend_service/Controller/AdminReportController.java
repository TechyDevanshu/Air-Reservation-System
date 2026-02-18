package com.example.frontend_service.Controller;

import com.example.frontend_service.Dto.ReportDTO;
import com.example.frontend_service.Service.ReportClientService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reports/admin")
@RequiredArgsConstructor
public class AdminReportController {

    private final ReportClientService reportClientService;

    @GetMapping
    public String showReportsPage(HttpSession session) {

        String role = (String) session.getAttribute("ROLE");

        if (role == null || !role.equals("ROLE_ADMIN")) {
            return "redirect:/auth/login";
        }

        return "admin/reports";
    }

    @GetMapping("/destination")
    public String searchByDestination(@RequestParam String destination,
                                      HttpSession session,
                                      Model model) {

        String token = (String) session.getAttribute("JWT");

        List<ReportDTO> reports =
                reportClientService.getByDestination(destination, token);

        model.addAttribute("bookings", reports);
        model.addAttribute("filterType", "destination");
        model.addAttribute("filterValue", destination);

        return "admin/reports";
    }

    @GetMapping("/date")
    public String searchByDate(@RequestParam String date,
                               HttpSession session,
                               Model model) {

        String token = (String) session.getAttribute("JWT");

        List<ReportDTO> reports =
                reportClientService.getByDate(date, token);

        model.addAttribute("bookings", reports);
        model.addAttribute("filterType", "date");
        model.addAttribute("filterValue", date);

        return "admin/reports";
    }
}
