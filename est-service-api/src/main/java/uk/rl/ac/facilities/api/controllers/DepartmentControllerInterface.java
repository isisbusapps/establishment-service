package uk.rl.ac.facilities.api.controllers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import uk.rl.ac.facilities.api.dto.CreateDepartmentDTO;
import uk.rl.ac.facilities.api.dto.DepartmentDTO;

import java.util.List;


@Path("department")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public interface DepartmentControllerInterface {

    @GET
    @Path("/{departmentId}")
    DepartmentDTO getDepartment(@PathParam("departmentId") Long departmentId);

    @PUT
    @Path("/{departmentId}/label")
    DepartmentDTO addDepartmentLabelLinks(@PathParam("departmentId") Long departmentId, List<Long> LabelIds);

    @DELETE
    @Path("/{departmentId}/label/{labelId}")
    DepartmentDTO removeDepartmentLabelLink(@PathParam("departmentId") Long departmentId, @PathParam("labelId") Long labelId);

    @DELETE
    @Path("/{departmentId}/label/")
    DepartmentDTO removeDepartmentLabelLink(@PathParam("departmentId") Long departmentId);

    @POST
    DepartmentDTO createDepartment(CreateDepartmentDTO request);

    @DELETE
    @Path("/{departmentId}")
    void deleteDepartment(@PathParam("departmentId") Long departmentId);

}
