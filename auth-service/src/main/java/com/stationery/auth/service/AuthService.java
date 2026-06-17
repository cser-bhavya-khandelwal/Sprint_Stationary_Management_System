package com.stationery.auth.service;

import com.stationery.auth.dto.LoginRequest;
import com.stationery.auth.dto.LoginResponse;
import com.stationery.auth.dto.RegisterRequest;
import com.stationery.auth.dto.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
