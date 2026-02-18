package com.example.user_service.Service;

import com.example.user_service.Entity.Role;
import com.example.user_service.Entity.User;
import com.example.user_service.Exception.InvalidCredentialsException;
import com.example.user_service.Exception.UserAlreadyExistsException;
import com.example.user_service.Repository.UserRepository;
import com.example.user_service.Utility.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already registered");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole() == null ? Role.PASSENGER : user.getRole());
        userRepository.save(user);
        return "Registration Successful";
    }

    @Transactional
    public String createAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        return "Admin Created Successfully";
    }

    @Transactional
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid Email or Password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid Email or Password");
        }

        return jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
    }

}
