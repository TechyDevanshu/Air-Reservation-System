package com.example.frontend_service.Helper;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Base64;

@Component
public class JwtHelper {

    public String extractRole(String token) {

        String[] chunks = token.split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));
        System.out.println(payload);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(payload);
            return jsonNode.get("role").asText();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token");
        }
    }

    public String extractEmail(String token) {

        String[] chunks = token.split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(payload);
            return jsonNode.get("sub").asText();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token");
        }
    }

    public Long extractUserId(String token) {

        String[] chunks = token.split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(payload);
            return jsonNode.get("userId").asLong();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token");
        }
    }

}
