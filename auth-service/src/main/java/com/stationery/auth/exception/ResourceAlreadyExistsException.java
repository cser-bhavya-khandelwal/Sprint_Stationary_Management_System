package com.stationery.auth.exception;

/**
 * Exception thrown when a resource already exists (e.g. registration with an existing email).
 */
public class ResourceAlreadyExistsException extends RuntimeException {
    
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
