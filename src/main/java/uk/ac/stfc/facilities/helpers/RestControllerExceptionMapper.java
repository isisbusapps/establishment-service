package uk.ac.stfc.facilities.helpers;

import uk.ac.stfc.facilities.exceptions.RestControllerException;
import jakarta.annotation.Priority;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;


@Provider
@Priority(2000)
public class RestControllerExceptionMapper implements ExceptionMapper<RestControllerException>{
    @Override
    public Response toResponse(RestControllerException exception) {
        return Response
                .status(exception.getHttpStatusCode())
                .entity(new ErrorDTO(exception.getShortCode(), exception.getMessage(), exception.getDetails()))
                .build();
    }
}
