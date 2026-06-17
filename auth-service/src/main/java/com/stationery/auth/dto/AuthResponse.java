package com.stationery.auth.dto;

/**
 * DTO representing authorization/authentication response.
 */
public class AuthResponse {
    
    private String message;
    private boolean success;

    public AuthResponse() {
    }

    public AuthResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
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
