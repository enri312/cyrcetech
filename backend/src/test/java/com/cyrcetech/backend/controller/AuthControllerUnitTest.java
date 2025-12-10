package com.cyrcetech.backend.controller;

import com.cyrcetech.backend.dto.request.LoginRequest;
import com.cyrcetech.backend.dto.request.RegisterRequest;
import com.cyrcetech.backend.dto.response.AuthResponse;
import com.cyrcetech.backend.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerUnitTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void register_ShouldReturnToken() {
        RegisterRequest request = new RegisterRequest("Admin", "admin@test.com", "password");
        AuthResponse response = new AuthResponse("token123", "id1", "admin@test.com", "ROLE_ADMIN");

        when(authService.register(request)).thenReturn(response);

        ResponseEntity<AuthResponse> result = authController.register(request);

        assertEquals(200, result.getStatusCode().value());
        AuthResponse body = result.getBody();
        assertNotNull(body);
        assertEquals("token123", body.getToken());
        verify(authService).register(request);
    }

    @Test
    void login_ShouldReturnToken() {
        LoginRequest request = new LoginRequest("admin", "password");
        AuthResponse response = new AuthResponse("token123", "id1", "admin", "ROLE_ADMIN");

        when(authService.login(request)).thenReturn(response);

        ResponseEntity<AuthResponse> result = authController.login(request);

        assertEquals(200, result.getStatusCode().value());
        AuthResponse body = result.getBody();
        assertNotNull(body);
        assertEquals("token123", body.getToken());
        verify(authService).login(request);
    }
}
