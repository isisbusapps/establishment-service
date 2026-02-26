package uk.rl.ac.facilities.rest.exceptions;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class InvalidInputExceptionMapper
        implements ExceptionMapper<InvalidInputException> {

    @Override
    public Response toResponse(InvalidInputException exception) {
        Map<String, String> error = Map.of("message", exception.getMessage());

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
