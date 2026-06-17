package com.stationery.inventory.exception;

/**
 * Exception thrown when there is not enough stock to perform a decrement operation.
 */
public class InsufficientStockException extends RuntimeException {
    
    public InsufficientStockException(String message) {
        super(message);
    }
}
