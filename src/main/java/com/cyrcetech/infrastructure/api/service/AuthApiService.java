package com.cyrcetech.infrastructure.api.service;

import com.cyrcetech.infrastructure.api.ApiClient;
import com.cyrcetech.infrastructure.api.ApiConfig;
import com.cyrcetech.infrastructure.api.dto.AuthResponseDTO;
import com.cyrcetech.infrastructure.api.dto.LoginRequestDTO;
import com.cyrcetech.infrastructure.session.SessionManager;

/**
 * Service to interact with Authentication REST API
 */
public class AuthApiService extends ApiClient {

    /**
     * Login with email and password
     * 
     * @param email    User email (or username)
     * @param password User password
     * @return AuthResponseDTO with token and user info
     * @throws Exception if login fails
     */
    public AuthResponseDTO login(String email, String password) throws Exception {
        String url = ApiConfig.getAuthUrl() + "/login";
        LoginRequestDTO request = new LoginRequestDTO(email, password);
        AuthResponseDTO response = post(url, request, AuthResponseDTO.class);

        // Store session
        SessionManager.getInstance().setSession(
                response.getToken(),
                response.getUserId(),
                response.getEmail(),
                response.getRole(),
                response.getFullName());

        return response;
    }

    /**
     * Register new user
     * 
     * @param fullName User's full name
     * @param email    User email
     * @param password User password
     * @return AuthResponseDTO with token and user info
     * @throws Exception if registration fails
     */
    public AuthResponseDTO register(String fullName, String email, String password) throws Exception {
        String url = ApiConfig.getAuthUrl() + "/register";
        RegisterRequestDTO request = new RegisterRequestDTO(fullName, email, password);
        AuthResponseDTO response = post(url, request, AuthResponseDTO.class);

        // Store session
        SessionManager.getInstance().setSession(
                response.getToken(),
                response.getUserId(),
                response.getEmail(),
                response.getRole(),
                response.getFullName());

        return response;
    }

    /**
     * Logout current user
     */
    public void logout() {
        SessionManager.getInstance().logout();
    }

    /**
     * Inner class for register request
     */
    private static class RegisterRequestDTO {
        private String fullName;
        private String email;
        private String password;

        public RegisterRequestDTO(String fullName, String email, String password) {
            this.fullName = fullName;
            this.email = email;
            this.password = password;
        }

        public String getFullName() {
            return fullName;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
}
