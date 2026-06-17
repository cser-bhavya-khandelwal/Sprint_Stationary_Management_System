package com.stationery.auth.service;

import com.stationery.auth.dto.AuthResponse;
import com.stationery.auth.dto.AuthenticationResponse;
import com.stationery.auth.dto.LoginRequest;
import com.stationery.auth.dto.RegisterRequest;

/**
 * Service interface defining authentication actions.
 */
public interface AuthService {

    /**
     * Registers a new user.
     *
     * @param request the registration details
     * @return AuthResponse containing success status and message
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authenticates an existing user and returns a JWT token.
     *
     * @param request the login credentials
     * @return AuthenticationResponse containing the JWT token, success status, and message
     */
    AuthenticationResponse login(LoginRequest request);
}
