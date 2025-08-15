package uk.ac.stfc.facilities.exceptions;

public class RorQueryException extends RuntimeException {
    public RorQueryException(String message, Exception e) {
        super(message);
    }
}
