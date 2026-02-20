package uk.rl.ac.facilities.api.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entityType, String id) {
        super(entityType + " with id " + id + " not found");
    }
}
