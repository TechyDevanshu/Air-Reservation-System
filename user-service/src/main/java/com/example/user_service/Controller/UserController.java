package com.example.user_service.Controller;

import com.example.user_service.Dto.LoginRequest;
import com.example.user_service.Entity.User;
import com.example.user_service.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public String register(@Valid @RequestBody User user) {
        return userService.register(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-admin")
    public String createAdmin(@Valid @RequestBody User user) {
        return userService.createAdmin(user);
    }
    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

}
