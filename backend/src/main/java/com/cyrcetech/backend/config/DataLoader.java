package com.cyrcetech.backend.config;

import com.cyrcetech.backend.domain.entity.Role;
import com.cyrcetech.backend.domain.entity.User;
import com.cyrcetech.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Data loader to create default users on application startup.
 * This creates the following users:
 * - Admin: username "admin", password "admin123"
 * - Admin: username "CENV", password "8994C"
 * - Technician: username "NMNV", password "8994N"
 * - User: username "LENV", password "8994L"
 */
@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createUserIfNotExists("admin", "Administrador", "admin123", Role.ROLE_ADMIN);
        createUserIfNotExists("CENV", "Administrador CENV", "8994C", Role.ROLE_ADMIN);
        createUserIfNotExists("NMNV", "Técnico NMNV", "8994N", Role.ROLE_TECHNICIAN);
        createUserIfNotExists("LENV", "Usuario LENV", "8994L", Role.ROLE_USER);

        log.info("Default users initialization completed");
    }

    private void createUserIfNotExists(String username, String fullName, String password, Role role) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setFullName(fullName);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            userRepository.save(user);
            log.info("Created default user: {} with role {}", username, role);
        } else {
            log.info("User {} already exists, skipping creation", username);
        }
    }
}
