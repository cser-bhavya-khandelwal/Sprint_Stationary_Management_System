package com.stationery.auth.service;

import com.stationery.auth.dto.AuthResponse;
import com.stationery.auth.dto.AuthenticationResponse;
import com.stationery.auth.dto.LoginRequest;
import com.stationery.auth.dto.RegisterRequest;
import com.stationery.auth.entity.User;
import com.stationery.auth.enums.Role;
import com.stationery.auth.exception.ResourceAlreadyExistsException;
import com.stationery.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_Success() {
        RegisterRequest request = new RegisterRequest("Test User", "test@test.com", "password", Role.STUDENT);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encrypted_password");
        
        AuthResponse response = authService.register(request);
        
        assertTrue(response.isSuccess());
        assertEquals("User registered successfully", response.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_ThrowsException_WhenEmailExists() {
        RegisterRequest request = new RegisterRequest("Test User", "test@test.com", "password", Role.STUDENT);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_Success() {
        LoginRequest request = new LoginRequest("test@test.com", "password");
        User user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@test.com")
                .role(Role.STUDENT)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(), any())).thenReturn("mocked_jwt_token");

        AuthenticationResponse response = authService.login(request);

        assertTrue(response.isSuccess());
        assertEquals("Login successful", response.getMessage());
        assertEquals("mocked_jwt_token", response.getToken());
        assertEquals("Test User", response.getName());
        assertEquals("test@test.com", response.getEmail());
    }

    @Test
    void login_Failure_AuthenticationException() {
        LoginRequest request = new LoginRequest("test@test.com", "wrong_password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));

        AuthenticationResponse response = authService.login(request);

        assertFalse(response.isSuccess());
        assertNull(response.getToken());
        assertEquals("Invalid email or password", response.getMessage());
    }
}
