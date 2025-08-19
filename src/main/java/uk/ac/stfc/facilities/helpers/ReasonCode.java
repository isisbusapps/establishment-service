package uk.ac.stfc.facilities.helpers;

import jakarta.ws.rs.core.Response;

public enum ReasonCode {
    ActivationExpired("Activation identifier has timed out", Response.Status.GONE),
    ActivationRedeemed("Activation identifier was already used", Response.Status.CONFLICT),
    AccountAlreadyActive("The account is already activated", Response.Status.CONFLICT),
    InsecurePassword("Password does not meet security requirements", Response.Status.BAD_REQUEST),
    SessionNotFound("Session identifier not recognised", Response.Status.NOT_FOUND),
    SessionExpired("Session identifier has been timed out", Response.Status.UNAUTHORIZED),
    Forbidden("Not enough permission to access this service", Response.Status.FORBIDDEN),
    Unauthorized("Authorization details were wrong", Response.Status.UNAUTHORIZED),
    DuplicateResults("Duplicate results found", Response.Status.CONFLICT),
    NoResults("No results found", Response.Status.NOT_FOUND),
    BadRequest("Bad request", Response.Status.BAD_REQUEST),
    UnexpectedError("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR),
    ServiceUnavailable("Service is currently unavailable", Response.Status.SERVICE_UNAVAILABLE),
    NotImplemented("This service is currently not configured", Response.Status.NOT_IMPLEMENTED),
    AccountNotActive("Account is not active", Response.Status.CONFLICT),
    BadGateway("Bad gateway", Response.Status.BAD_GATEWAY),
    InvalidToken("The token submitted is either invalid or does not exist", Response.Status.CONFLICT),
    ResetIDUsed("Reset ID has already been used", Response.Status.CONFLICT),
    ResetIDExpired("The reset ID has expired", Response.Status.GONE),
    EmailInUse("Email address already in use", Response.Status.CONFLICT);

    private final String description;
    private final Response.Status status;

    public String getDescription() { return description; }
    public Response.Status getStatus() { return status; }
    ReasonCode(String description, Response.Status status) {
        this.description = description;
        this.status = status;
    }
}
