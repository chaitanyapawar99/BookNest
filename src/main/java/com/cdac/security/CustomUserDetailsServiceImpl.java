package com.cdac.security;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.dao.UserDao;
import com.cdac.entities.User;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsServiceImpl.class);

    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null || email.isBlank()) {
            throw new UsernameNotFoundException("Invalid credentials");
        }

        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

        User user = userDao.findByEmail(normalizedEmail)
                .orElseThrow(() -> {
                    log.debug("User not found for email: {}", normalizedEmail);
                    return new UsernameNotFoundException("Invalid credentials");
                });

        log.debug("Loaded user id={} for email={}", user.getId(), normalizedEmail);
        return user;
    }
}
