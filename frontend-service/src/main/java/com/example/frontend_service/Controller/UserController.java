package com.example.frontend_service.Controller;


import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/dashboard")
    public String userDashboard(HttpSession session, Model model) {

        if (!isPassenger(session)) {
            return "redirect:/access-denied";
        }

        model.addAttribute("email", session.getAttribute("EMAIL"));

        return "user/dashboard";
    }

    private boolean isPassenger(HttpSession session) {
        if (session == null || session.getAttribute("JWT") == null) {
            return false;
        }

        String role = (String) session.getAttribute("ROLE");
        if (role == null) {
            return false;
        }

        role = role.replace("ROLE_", "");
        System.out.println(role);
        return "PASSENGER".equals(role);
    }
}
