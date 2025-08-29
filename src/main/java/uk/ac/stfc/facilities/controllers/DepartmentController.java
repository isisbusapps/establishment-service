package uk.ac.stfc.facilities.controllers;

import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import uk.ac.stfc.facilities.domains.department.Department;
import uk.ac.stfc.facilities.domains.department.DepartmentLabel;
import uk.ac.stfc.facilities.domains.department.DepartmentLabelId;
import uk.ac.stfc.facilities.domains.department.DepartmentService;
import uk.ac.stfc.facilities.exceptions.RestControllerException;
import uk.ac.stfc.facilities.helpers.CreateDepartmentRequest;
import uk.ac.stfc.facilities.helpers.CreateDepartmentResponse;
import uk.ac.stfc.facilities.helpers.ReasonCode;

import java.util.List;

@Transactional
public class DepartmentController implements DepartmentControllerInterface {

    @Inject
    DepartmentService service;

    @Override
    public Response addDepartmentLabelsManually(Long departmentId, List<Long> labelIds) throws RestControllerException {
        if (departmentId == null || labelIds == null) {
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

    @Override
    public Response removeDepartmentLabel(Long departmentId, Long labelId) throws RestControllerException {

        DepartmentLabelId id = new DepartmentLabelId(departmentId, labelId);

        if (service.getDepartmentLabel(id) == null) {
            throw new RestControllerException(ReasonCode.NoResults, "No such DepartmentLabel found");
        }

        service.deleteDepartmentLabel(id);
        boolean fallbackAdded = service.addFallbackLabelIfNeeded(departmentId);

        if (fallbackAdded) {
            return Response.ok()
                    .entity("{\"message\":\"DepartmentLabel removed successfully and fallback added since no labels were remaining after deletion\"}")
                    .build();
        }

        return Response.ok()
                .entity("{\"message\":\"DepartmentLabel removed successfully\"}")
                .build();
    }

    @Override
    public Response createDepartmentAndDepLabels(CreateDepartmentRequest request) throws RestControllerException {
        String name = request.getName();
        Long establishmentId = request.getEstablishmentId();

        if (name == null || establishmentId == null) {
            throw new RestControllerException(ReasonCode.BadRequest, "Request must have a name and departmentId");
        }

        try{
            Department department = service.createDepartment(name, establishmentId);
            List<DepartmentLabel> departmentLabels = service.addDepartmentLabelsAutomatically(department.getDepartmentId());
            CreateDepartmentResponse response = new CreateDepartmentResponse(department, departmentLabels);

            return Response.status(Response.Status.OK).entity(response).build();
        } catch (IllegalArgumentException e) {
            throw new RestControllerException(ReasonCode.DuplicateResults, e.getMessage());
        }
    }

    @Override
    public Response deleteDepartmentAndDepLabels(Long departmentId) throws RestControllerException {
        if (departmentId == null) {
            throw new RestControllerException(ReasonCode.BadRequest, "Missing input department id");
        }

        if (service.getDepartment(departmentId) == null) {
            throw new RestControllerException(ReasonCode.NoResults, "No such Department found");
        }

        boolean deleted = service.deleteDepartment(departmentId);

        return Response.ok()
                .entity("{\"message\":\"Department and associated DepartmentLabels removed successfully\"}")
                .build();
    }
}

