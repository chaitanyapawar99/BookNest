package com.cdac.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AllArgsConstructor
public class CustomJwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Skip processing for preflight requests
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                filterChain.doFilter(request, response);
                return;
            }

            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7).trim();
                if (!token.isEmpty()) {
                    try {
                        Authentication auth = jwtUtils.populateAuthenticationTokenFromJWT(token);
                        if (auth != null && auth.isAuthenticated()) {
                            SecurityContextHolder.getContext().setAuthentication(auth);
                        } else if (auth != null) {
                            // optionally set it even if not authenticated, or log
                            SecurityContextHolder.getContext().setAuthentication(auth);
                        }
                    } catch (JwtException ex) {
                        // token invalid / expired / signature invalid
                        log.debug("Invalid JWT: {}", ex.getMessage()); // debug only
                        // clear context to be safe
                        SecurityContextHolder.clearContext();
                        // optionally you can send 401 here, but usually we let AuthEntryPoint handle it
                    }
                }
            }

            // continue filter chain
            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            // unexpected errors: ensure context cleared and let the exception bubble to entry point/handler
            log.error("Error in JWT filter: {}", ex.getMessage(), ex);
            SecurityContextHolder.clearContext();
            throw ex;
        }
    }
}
