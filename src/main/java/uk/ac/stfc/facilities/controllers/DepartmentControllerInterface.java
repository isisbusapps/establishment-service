package uk.ac.stfc.facilities.controllers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import uk.ac.stfc.facilities.domains.establishment.RorSchemaV21;
import uk.ac.stfc.facilities.exceptions.RestControllerException;



@Path("department")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public interface DepartmentControllerInterface {

    @PUT
    @Path("/{departmentId}/add-label")
    Response addDepartmentLabelsAutomatically(@PathParam("departmentId") Long departmentId)
            throws RestControllerException;

}
