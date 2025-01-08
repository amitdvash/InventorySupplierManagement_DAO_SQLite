package dev.Suppliers.Domain.Exception;

// Custom exception for handling exit in a generic manner
public class ExitException extends RuntimeException {
    public ExitException(String message) {
        super(message);
    }
}
