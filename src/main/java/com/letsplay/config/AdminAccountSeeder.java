package com.letsplay.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.letsplay.model.Role;
import com.letsplay.model.User;
import com.letsplay.repository.UserRepository;

/**
 * Bootstraps a default ADMIN account on startup if one doesn't already exist,
 * so there's always a way to obtain an admin JWT for testing role-protected endpoints.
 * Credentials are sourced from admin.username / admin.email / admin.password
 * (overridable via ADMIN_USERNAME / ADMIN_EMAIL / ADMIN_PASSWORD env vars).
 */
@Component
public class AdminAccountSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminAccountSeeder.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminEmail;
    private final String adminPassword;

    public AdminAccountSeeder(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${admin.username}") String adminUsername,
            @Value("${admin.email}") String adminEmail,
            @Value("${admin.password}") String adminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        if (userRepository.existsByUsername(adminUsername)) {
            return;
        }

        User admin = new User(adminUsername, adminEmail, passwordEncoder.encode(adminPassword), Role.ADMIN);
        userRepository.save(admin);
        log.info("Bootstrapped default admin account '{}'", adminUsername);
    }
}
