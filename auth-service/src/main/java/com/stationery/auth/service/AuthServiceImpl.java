package com.stationery.auth.service;

import com.stationery.auth.dto.AuthResponse;
import com.stationery.auth.dto.AuthenticationResponse;
import com.stationery.auth.dto.LoginRequest;
import com.stationery.auth.dto.RegisterRequest;
import com.stationery.auth.entity.User;
import com.stationery.auth.exception.ResourceAlreadyExistsException;
import com.stationery.auth.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service implementation for user authentication supporting JWT generation.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * Constructor Injection for dependencies.
     */
    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email is already registered: " + request.getEmail());
        }

        // Encrypt the password before saving for production-ready security
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);

        return new AuthResponse("User registered successfully", true);
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        try {
            // Perform authentication using standard Spring Security AuthenticationManager
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            return new AuthenticationResponse(null, "Invalid email or password", false, null, null, null);
        }

        // Fetch user details to generate token
        return userRepository.findByEmail(request.getEmail())
                .map(user -> {
                    java.util.Map<String, Object> extraClaims = new java.util.HashMap<>();
                    extraClaims.put("role", user.getRole().name());
                    extraClaims.put("name", user.getName());
                    String jwtToken = jwtService.generateToken(extraClaims, user);
                    return new AuthenticationResponse(jwtToken, "Login successful", true, user.getName(), user.getEmail(), user.getRole().name());
                })
                .orElse(new AuthenticationResponse(null, "Invalid email or password", false, null, null, null));
    }
}
