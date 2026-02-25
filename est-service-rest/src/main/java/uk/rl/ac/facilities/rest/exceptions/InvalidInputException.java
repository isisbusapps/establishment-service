package uk.rl.ac.facilities.rest.exceptions;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}
