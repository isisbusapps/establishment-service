package uk.ac.stfc.facilities.helpers;

import  uk.ac.stfc.facilities.BaseClasses.Dto;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * The DTO for the error response from REST services.
 * <p/>
 * Returns structured, predictable error information to clients of the REST service to assist in exception management,
 * reporting and diagnosis.
 */
@Schema(description = "The DTO for the error response from REST services")
public class ErrorDTO implements Dto, Serializable {

    @Schema(description = "HTTP status code to be returned to the client")
    private final String shortcode;
    @Schema(description = "A string used to identify the cause of the failure to the client")
    private final String reason;
    @Schema(description = "A detailed message of the cause of the failure to the client")
    private List<String> details;

    public String getShortcode() { return shortcode; }
    public String getReason() { return reason; }
    public List<String> getDetails() { return details; }
    public void setDetails(List<String> details) { this.details = details; }
    public void setDetails(String details) { this.setDetails(Arrays.asList(details)); }

    /**
     * Creates a custom error DTO with specific properties.
     * @param shortCode A string used to identify the cause of the exception to clients.
     * @param reason A short message giving the reason for the exception.
     * @param details A string list containing further info on causes and environment.
     */
    public ErrorDTO(String shortCode, String reason, List<String> details) {
        this.shortcode = shortCode;
        this.reason = reason;
        this.details = details;
    }
    /**
     * Creates a custom error DTO with specific properties.
     * @param shortCode A string used to identify the cause of the exception to clients.
     * @param reason A short message giving the reason for the exception.
     * @param details A string list containing further info on causes and environment.
     */
    public ErrorDTO(String shortCode, String reason, String details) {
        this(shortCode, reason, Arrays.asList(details));
    }
    /**
     * Creates a custom error DTO with specific properties.
     * @param shortCode A string used to identify the cause of the exception to clients.
     * @param reason A short message giving the reason for the exception.
     */
    public ErrorDTO(String shortCode, String reason) {
        this(shortCode, reason, Arrays.asList());
    }
}
