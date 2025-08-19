package uk.ac.stfc.facilities.exceptions;

import uk.ac.stfc.facilities.helpers.ReasonCode;
import jakarta.ws.rs.core.Response;

import java.util.Arrays;
import java.util.List;

/**
 * An exception designed to be thrown by a REST controller.
 * <p/>
 * It extends the general Exception class to include an HTTP status, shortcode, and list of detail info for inclusion in
 * the standard ErrorDTO response object type.
 */
public class RestControllerException extends Exception {

    private int httpStatusCode;
    private String shortCode;
    private List<String> details;

    /**
     * Constructs a custom instance of RestControllerException with specific properties.
     * @param httpStatusCode The HTTP status code to be returned to the client by this exception.
     * @param shortCode A string used to identify the cause of the exception to clients.
     * @param message A short message giving the reason for the exception.
     * @param details A string list containing further info on causes and environment.
     */
    public RestControllerException(int httpStatusCode, String shortCode, String message, List<String> details)
    {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.shortCode = shortCode;
        this.details = details;
    }
    /**
     * Constructs a custom instance of RestControllerException with specific properties.
     * @param httpStatusCode The HTTP status code to be returned to the client by this exception.
     * @param shortCode A string used to identify the cause of the exception to clients.
     * @param message A short message giving the reason for the exception.
     * @param details A string containing further info on causes and environment.
     */
    public RestControllerException(int httpStatusCode, String shortCode, String message, String details) {
        this(httpStatusCode, shortCode, message, Arrays.asList(details));
    }
    /**
     * Constructs a custom instance of RestControllerException with specific properties.
     * @param status The Jax-RS status to be returned to the client by this exception.
     * @param shortCode A string used to identify the cause of the exception to clients.
     * @param message A short message giving the reason for the exception.
     * @param details A string list containing further info on causes and environment.
     */
    public RestControllerException(Response.Status status, String shortCode, String message, List<String> details) {
        this(status.getStatusCode(), shortCode, message, details);
    }
    /**
     * Constructs a custom instance of RestControllerException with specific properties.
     * @param status The Jax-RS status to be returned to the client by this exception.
     * @param shortCode A string used to identify the cause of the exception to clients.
     * @param message A short message giving the reason for the exception.
     * @param details A string containing further info on causes and environment.
     */
    public RestControllerException(Response.Status status, String shortCode, String message, String details){
        this(status, shortCode, message, Arrays.asList(details));
    }
    /**
     * Creates an instance of RestControllerException from a predefined list of possibilities and a custom Jax-RS return status.
     * @param status The Jax-RS status to be returned to the client by this exception.
     * @param reasonCode The selected configuration of shortcode and message to be used by the exception.
     * @param details A string list containing further info on causes and environment.
     */
    public RestControllerException(Response.Status status, ReasonCode reasonCode, List<String> details) {
        this(status, reasonCode.name(), reasonCode.getDescription(), details);
    }
    /**
     * Creates an instance of RestControllerException from a predefined list of possibilities and a custom Jax-RS return status.
     * @param status The Jax-RS status to be returned to the client by this exception.
     * @param reasonCode The selected configuration of shortcode and message to be used by the exception.
     * @param details A string containing further info on causes and environment.
     */
    public RestControllerException(Response.Status status, ReasonCode reasonCode, String details) {
        this(status, reasonCode.name(), reasonCode.getDescription(), details);
    }
    /**
     * Creates an instance of RestControllerException from a predefined list of possibilities and a custom Jax-RS return status.
     * @param status The Jax-RS status to be returned to the client by this exception.
     * @param reasonCode The selected configuration of shortcode and message to be used by the exception.
     */
    public RestControllerException(Response.Status status, ReasonCode reasonCode) {
        this(status, reasonCode.name(), reasonCode.getDescription(), Arrays.asList());
    }
    /**
     * Creates an instance of RestControllerException from a predefined list of possibilities.
     * @param reasonCode The selected configuration of HTTP status, shortcode and message to be used by the exception.
     * @param details A string list containing further info on causes and environment.
     */
    public RestControllerException(ReasonCode reasonCode, List<String> details) {
        this(reasonCode.getStatus(), reasonCode.name(), reasonCode.getDescription(), details);
    }
    /**
     * Creates an instance of RestControllerException from a predefined list of possibilities.
     * @param reasonCode The selected configuration of HTTP status, shortcode and message to be used by the exception.
     * @param details A string containing further info on causes and environment.
     */
    public RestControllerException(ReasonCode reasonCode, String details) {
        this(reasonCode.getStatus(), reasonCode.name(), reasonCode.getDescription(), details);
    }
    /**
     * Creates an instance of RestControllerException from a predefined list of possibilities.
     * @param reasonCode The selected configuration of HTTP status, shortcode and message to be used by the exception.
     */
    public RestControllerException(ReasonCode reasonCode) {
        this(reasonCode.getStatus(), reasonCode.name(), reasonCode.getDescription(), Arrays.asList());
    }

    public int getHttpStatusCode() { return httpStatusCode; }
    public String getShortCode() { return shortCode; }
    public List<String> getDetails() { return details; }
    public void setHttpStatusCode(int httpStatusCode) { this.httpStatusCode = httpStatusCode; }
    public void setShortCode(String shortCode) { this.shortCode = shortCode; }
    public void setDetails(List<String> details) { this.details = details; }
}