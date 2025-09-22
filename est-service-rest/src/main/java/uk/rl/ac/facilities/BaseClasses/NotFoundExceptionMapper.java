package uk.rl.ac.facilities.BaseClasses;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uk.rl.ac.facilities.api.exceptions.EntityNotFoundException;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {

    @Override
    public Response toResponse(EntityNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND).entity(exception.getMessage()).build();
    }
}
