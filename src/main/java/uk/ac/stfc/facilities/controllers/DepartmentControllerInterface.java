package uk.ac.stfc.facilities.controllers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import uk.ac.stfc.facilities.domains.department.DepartmentDTO;
import uk.ac.stfc.facilities.exceptions.RestControllerException;
import uk.ac.stfc.facilities.helpers.CreateDepartmentRequest;

import java.util.List;


@Path("department")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public interface DepartmentControllerInterface {

    @GET
    @Path("/{departmentId}")
    DepartmentDTO getDepartment(@PathParam("departmentId") Long departmentId)
            throws RestControllerException;

    @PUT
    @Path("/{departmentId}/add-label-manual")
    Response addDepartmentLabelLinksManually(@PathParam("departmentId") Long departmentId, List<Long> LabelIds)
            throws RestControllerException;

    @PUT
    @Path("/{departmentId}/add-label-auto")
    Response addDepartmentLabelLinksAutomatically(@PathParam("departmentId") Long departmentId)
            throws RestControllerException;

    @DELETE
    @Path("/{departmentId}/labels/{labelId}")
    Response removeDepartmentLabelLink(@PathParam("departmentId") Long departmentId, @PathParam("labelId") Long labelId)
            throws RestControllerException;

    @POST
    Response createDepartmentAndDepLabelLinks(CreateDepartmentRequest request)
        throws RestControllerException;

    @DELETE
    @Path("/{departmentId}")
    Response deleteDepartmentAndDepLabelLinks(@PathParam("departmentId") Long departmentId)
            throws RestControllerException;

}
