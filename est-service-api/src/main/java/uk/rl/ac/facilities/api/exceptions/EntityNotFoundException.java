package uk.rl.ac.facilities.api.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entityType, Object id) {
        super(String.format("%s with id '%s' not found" , entityType, id));
    }
}
