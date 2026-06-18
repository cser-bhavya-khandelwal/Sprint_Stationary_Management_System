package com.stationery.request.exception;

/**
 * Exception thrown when requested quantity exceeds available stock in Inventory.
 */
public class InsufficientStockException extends RuntimeException {
    
    public InsufficientStockException(String message) {
        super(message);
    }
}
