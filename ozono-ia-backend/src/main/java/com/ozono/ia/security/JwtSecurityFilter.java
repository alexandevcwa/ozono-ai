package com.ozono.ia.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtSecurityService jwtSecurityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null) {
            filterChain.doFilter(request, response);
            return;
        } else if (!authorizationHeader.startsWith("Bearer ")) {
            request.setAttribute("error", "Invalid token format");
            filterChain.doFilter(request, response);
            return;
        }


        try {
            Authentication auth = jwtSecurityService.validateToken(authorizationHeader);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (ExpiredJwtException e) {
            SecurityContextHolder.clearContext();
            request.setAttribute("error", "Session expired, please login again");
        } catch (MalformedJwtException e) {
            SecurityContextHolder.clearContext();
            request.setAttribute("error", "Token is malformed, please login again");
        }
        filterChain.doFilter(request, response);
    }
}
