package uk.ac.stfc.facilities.controllers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import uk.ac.stfc.facilities.exceptions.RestControllerException;

import java.util.List;


@Path("department")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public interface DepartmentControllerInterface {

    @PUT
    @Path("/{departmentId}/add-label-manual")
    Response addDepartmentLabelsManually(@PathParam("departmentId") Long departmentId, List<Long> LabelIds)
            throws RestControllerException;

    @PUT
    @Path("/{departmentId}/add-label-auto")
    Response addDepartmentLabelsAutomatically(@PathParam("departmentId") Long departmentId)
            throws RestControllerException;

    @DELETE
    @Path("/{departmentId}/labels/{labelId}")
    Response removeDepartmentLabel(@PathParam("departmentId") Long departmentId, @PathParam("labelId") Long labelId)
            throws RestControllerException;

}
