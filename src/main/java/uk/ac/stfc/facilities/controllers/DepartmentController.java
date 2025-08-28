package uk.ac.stfc.facilities.controllers;

import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import uk.ac.stfc.facilities.domains.department.DepartmentLabel;
import uk.ac.stfc.facilities.domains.department.DepartmentService;
import uk.ac.stfc.facilities.exceptions.RestControllerException;
import uk.ac.stfc.facilities.helpers.ReasonCode;

import java.util.List;

@Transactional
public class DepartmentController implements DepartmentControllerInterface {

    @Inject
    DepartmentService service;

    @Override
    public Response addDepartmentLabelsManually(Long departmentId, List<Long> labelIds) throws RestControllerException {
        if (departmentId == null) {
            throw new RestControllerException(ReasonCode.BadRequest, "Missing input department id");
        }
        try{
            List<DepartmentLabel> labels = service.addDepartmentLabels(departmentId, labelIds);
            return Response.status(Response.Status.OK).entity(labels).build();
        } catch (NoResultException e) {
            throw new RestControllerException(ReasonCode. NoResults, e.getMessage());
        }
    }

    @Override
    public Response addDepartmentLabelsAutomatically(Long departmentId) throws RestControllerException {
        if (departmentId == null) {
            throw new RestControllerException(ReasonCode.BadRequest, "Missing input department id");
        }
        try{
            List<DepartmentLabel> labels = service.addDepartmentLabelsAutomatically(departmentId);
            return Response.status(Response.Status.OK).entity(labels).build();
        } catch (NoResultException e) {
            throw new RestControllerException(ReasonCode. NoResults, e.getMessage());
        }
    }
}
