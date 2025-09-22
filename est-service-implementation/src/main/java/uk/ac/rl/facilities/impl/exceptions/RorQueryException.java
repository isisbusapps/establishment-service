package uk.ac.rl.facilities.impl.exceptions;

public class RorQueryException extends RuntimeException {
    public RorQueryException(String message, Exception e) {
        super(message);
    }
}
