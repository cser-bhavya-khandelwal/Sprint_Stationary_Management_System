package com.stationery.inventory.exception;

/**
 * Exception thrown when a requested inventory resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
