package com.cdac.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    
    @Autowired
    private CustomJwtFilter customJwtFilter;
    
    @Autowired
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.and())
            .authorizeHttpRequests(request -> request
                // public / swagger / docs
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // public auth endpoints (note leading slash)
                .requestMatchers("/api/users/signin", "/api/users/signup").permitAll()
                // allow preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // example public GET (adjust to your app)
                .requestMatchers(HttpMethod.GET, "/api/books", "/api/books/**").permitAll()
                // role based examples (ensure authorities are ROLE_*)
                .requestMatchers(HttpMethod.PUT, "/admin/**").hasRole("ADMIN")
                // everything else authenticated
                .anyRequest().authenticated()
            )
            // no sessions
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // add jwt filter before username/password filter
            .addFilterBefore(customJwtFilter, UsernamePasswordAuthenticationFilter.class)
            // customize auth entrypoint for 401
            .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthEntryPoint));
        
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration mgr) throws Exception {
        return mgr.getAuthenticationManager();
    }
}
