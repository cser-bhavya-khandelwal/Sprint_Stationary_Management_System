package com.stationery.auth.dto;

/**
 * DTO representing the response containing JWT token and user info upon successful login.
 */
public class AuthenticationResponse {

    private String token;
    private String message;
    private boolean success;
    private String name;
    private String email;
    private String role;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String token, String message, boolean success) {
        this.token = token;
        this.message = message;
        this.success = success;
    }

    public AuthenticationResponse(String token, String message, boolean success, String name, String email, String role) {
        this.token = token;
        this.message = message;
        this.success = success;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
