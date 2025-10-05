package com.cdac.security;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.cdac.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String jwtSecret;

    @Value("${jwt.expiration.time}")
    private long jwtExpirationMs;

    private SecretKey key;

    @PostConstruct
    public void init() {
        log.info("JwtUtils initialized with secret: {}", jwtSecret);
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // Generate JWT token from Authentication (after successful login)
    public String generateJwtToken(Authentication authentication) {
        log.info("generate jwt token " + authentication);
        User userPrincipal = (User) authentication.getPrincipal();

        return Jwts.builder()
        	    .subject(userPrincipal.getUsername())
        	    .issuedAt(new Date())
        	    .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
        	    .claim("authorities", getAuthoritiesInString(userPrincipal.getAuthorities()))
        	    .signWith(key)
        	    .compact();

    }

    // Validate and return claims. Throws JwtException (including ExpiredJwtException) on invalid/expired token.
    public Claims validateAndGetClaims(String jwtToken) {
    	return Jwts.parser()
    		    .verifyWith(key)
    		    .build()
    		    .parseSignedClaims(jwtToken)
    		    .getPayload();            // get the Claims (payload)
    }

    // Extract username (subject) from claims
    public String getUserNameFromClaims(Claims claims) {
        return claims.getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<GrantedAuthority> getAuthoritiesFromClaims(Claims claims) {
        Object raw = claims.get("authorities");
        if (raw instanceof List<?>) {
            // safe mapping to List<String>
            List<?> rawList = (List<?>) raw;
            List<String> names = rawList.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.toList());
            return names.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    private List<String> toAuthorityStrings(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    // Build Authentication object from token (used by filter)
    public Authentication populateAuthenticationTokenFromJWT(String jwt) throws JwtException {
        Claims claims = validateAndGetClaims(jwt);
        String username = getUserNameFromClaims(claims);
        List<GrantedAuthority> authorities = getAuthoritiesFromClaims(claims);
        // create Authentication with principal as username (or you could load full user if needed)
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    // small helper to detect Base64 strings (not perfect but useful)
    private boolean isBase64(String s) {
        return s != null && (s.matches("^[A-Za-z0-9+/=\\r\\n]+$") && s.length() % 4 == 0);
    }
    private List<String> getAuthoritiesInString(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

}
