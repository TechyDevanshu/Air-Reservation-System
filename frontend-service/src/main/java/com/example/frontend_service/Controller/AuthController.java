package com.example.frontend_service.Controller;

import com.example.frontend_service.Dto.LoginRequest;
import com.example.frontend_service.Dto.RegisterRequest;
import com.example.frontend_service.Helper.JwtHelper;
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
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserClientService userClientService;
    private final JwtHelper jwtHelper;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest request,
                        HttpSession session,
                        Model model) {

        try {
            String token = userClientService.login(request);

            String role = jwtHelper.extractRole(token);
            String email = jwtHelper.extractEmail(token);
            Long userId = jwtHelper.extractUserId(token);

            session.setAttribute("JWT", token);
            session.setAttribute("ROLE", role);
            session.setAttribute("EMAIL", email);
            session.setAttribute("USER_ID", userId);

            // Normalize role
            if (role != null && role.contains("ADMIN")) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/user/dashboard";
            }

        } catch (Exception e) {
            model.addAttribute("error", "Invalid email or password");
            return "auth/login";
        }
    }


    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest request,
                           Model model) {
        try {
            userClientService.register(request);
            return "redirect:/auth/login";
        } catch (Exception e) {
            model.addAttribute("error", "Email already registered");
            return "auth/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}
