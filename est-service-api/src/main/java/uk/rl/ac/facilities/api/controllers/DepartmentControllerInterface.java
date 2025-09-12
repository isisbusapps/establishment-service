package uk.rl.ac.facilities.api.controllers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import uk.rl.ac.facilities.api.dto.CreateDepartmentDTO;
import uk.rl.ac.facilities.api.dto.DepartmentDTO;
import uk.rl.ac.facilities.api.dto.DepartmentDetailsDTO;

import java.util.List;


@Path("department")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public interface DepartmentControllerInterface {

    @GET
    @Path("/{departmentId}")
    DepartmentDTO getDepartment(@PathParam("departmentId") Long departmentId);

    @GET
    @Path("/{departmentId}/details")
    DepartmentDetailsDTO getDepartmentDetails(@PathParam("departmentId") Long departmentId);

    @PUT
    @Path("/{departmentId}/add-label-manual")
    Response addDepartmentLabelLinksManually(@PathParam("departmentId") Long departmentId, List<Long> LabelIds);

    @PUT
    @Path("/{departmentId}/add-label-auto")
    Response addDepartmentLabelLinksAutomatically(@PathParam("departmentId") Long departmentId);

    @DELETE
    @Path("/{departmentId}/label/{labelId}")
    Response removeDepartmentLabelLink(@PathParam("departmentId") Long departmentId, @PathParam("labelId") Long labelId);

    @POST
    Response createDepartmentAndDepLabelLinks(CreateDepartmentDTO request);

    @DELETE
    @Path("/{departmentId}")
    Response deleteDepartmentAndDepLabelLinks(@PathParam("departmentId") Long departmentId);

}
