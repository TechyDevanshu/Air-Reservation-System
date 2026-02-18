package com.example.frontend_service.Controller;

import com.example.frontend_service.Dto.RegisterRequest;
import com.example.frontend_service.Service.UserClientService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserClientService userClientService;

    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session, Model model) {

        // 1️⃣ Check if session exists
        if (session == null || session.getAttribute("JWT") == null) {
            return "redirect:/auth/login";
        }

        String role = (String) session.getAttribute("ROLE");

        if (role == null) {
            session.invalidate();
            return "redirect:/auth/login";
        }

        // 2️⃣ Normalize role (handles ROLE_ADMIN / ADMIN)
        role = role.replace("ROLE_", "");

        if (!"ADMIN".equals(role)) {
            return "redirect:/access-denied";
        }

        // 3️⃣ Add required attributes
        model.addAttribute("email", session.getAttribute("EMAIL"));
        model.addAttribute("role", role);

        return "admin/dashboard";
    }

    @GetMapping("/create")
    public String createAdminPage(HttpSession session, Model model) {

        if (!isAdmin(session)) {
            return "redirect:/access-denied";
        }

        model.addAttribute("registerRequest", new RegisterRequest());
        return "admin/create-admin";
    }

    @PostMapping("/create")
    public String createAdmin(@ModelAttribute RegisterRequest request,
                              HttpSession session,
                              Model model) {

        if (!isAdmin(session)) {
            return "redirect:/access-denied";
        }

        try {
            String token = (String) session.getAttribute("JWT");

            userClientService.createAdmin(request, token);

            model.addAttribute("success", "Admin created successfully!");
            model.addAttribute("registerRequest", new RegisterRequest());

        } catch (Exception e) {
            model.addAttribute("error", "Failed to create admin.");
        }

        return "admin/create-admin";
    }

    private boolean isAdmin(HttpSession session) {

        if (session == null || session.getAttribute("JWT") == null)
            return false;

        String role = (String) session.getAttribute("ROLE");
        if (role == null)
            return false;

        role = role.replace("ROLE_", "");

        return "ADMIN".equals(role);
    }


}
