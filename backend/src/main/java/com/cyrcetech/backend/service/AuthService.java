package com.cyrcetech.backend.service;

import com.cyrcetech.backend.domain.entity.Role;
import com.cyrcetech.backend.domain.entity.User;
import com.cyrcetech.backend.dto.request.LoginRequest;
import com.cyrcetech.backend.dto.request.RegisterRequest;
import com.cyrcetech.backend.dto.response.AuthResponse;
import com.cyrcetech.backend.repository.UserRepository;
import com.cyrcetech.backend.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final AuditLogService auditLogService;

    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider,
            AuthenticationManager authenticationManager,
            AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.auditLogService = auditLogService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setUsername(request.getEmail()); // Use email as username for registration
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_ADMIN); // Default to ADMIN for now to simplify setup

        User savedUser = userRepository.save(user);
        String token = jwtTokenProvider.generateToken(savedUser);

        // Log registration
        auditLogService.logLogin(savedUser.getUsername(), savedUser.getRole(), savedUser.getId());

        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole().name(),
                savedUser.getFullName());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = jwtTokenProvider.generateToken(user);

        // Log login action
        auditLogService.logLogin(user.getUsername(), user.getRole(), user.getId());

        return new AuthResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getRole().name(),
                user.getFullName());
    }
}
