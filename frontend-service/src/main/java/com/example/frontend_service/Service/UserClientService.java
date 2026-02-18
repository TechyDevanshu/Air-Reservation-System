package com.example.frontend_service.Service;

import com.example.frontend_service.Dto.LoginRequest;
import com.example.frontend_service.Dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserClientService {

    private final WebClient webClient;

    public String login(LoginRequest request) {
        return webClient.post()
                .uri("/users/login")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String register(RegisterRequest request) {
        return webClient.post()
                .uri("/users/register")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String createAdmin(RegisterRequest request, String token) {
        return webClient.post()
                .uri("/users/create-admin")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}

