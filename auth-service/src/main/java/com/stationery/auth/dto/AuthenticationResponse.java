package com.stationery.auth.dto;

/**
 * DTO representing the response containing JWT token upon successful login.
 */
public class AuthenticationResponse {

    private String token;
    private String message;
    private boolean success;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String token, String message, boolean success) {
        this.token = token;
        this.message = message;
        this.success = success;
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
}
