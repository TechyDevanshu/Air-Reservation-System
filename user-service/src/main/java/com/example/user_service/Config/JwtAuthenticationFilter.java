package com.example.user_service.Config;

import com.example.user_service.Utility.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String email = null;
        String role = null;

        try {
            email = jwtUtil.extractEmail(token);
            role = jwtUtil.extractRole(token);
        } catch (Exception e) {
            System.out.println("Invalid JWT Token: " + e.getMessage());
        }

        UsernamePasswordAuthenticationToken authToken = null;

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            String authority = role;

            authToken = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    List.of(new SimpleGrantedAuthority(authority))
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // FIXED DEBUG
        if (authToken != null) {
            System.out.println("Authorities = " + authToken.getAuthorities());
        } else {
            System.out.println("AuthToken is NULL");
        }

        filterChain.doFilter(request, response);
    }
}
