package uk.ac.stfc.facilities.controllers;

import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import uk.ac.stfc.facilities.domains.department.*;
import uk.ac.stfc.facilities.domains.establishment.EstablishmentService;
import uk.ac.stfc.facilities.exceptions.RestControllerException;
import uk.ac.stfc.facilities.helpers.CreateDepartmentRequest;
import uk.ac.stfc.facilities.helpers.CreateDepartmentResponse;
import uk.ac.stfc.facilities.helpers.ReasonCode;

import java.util.List;

@Transactional
public class DepartmentController implements DepartmentControllerInterface {
    @Inject
    DepartmentMapper mapper;
    @Inject
    DepartmentService depService;
    @Inject
    EstablishmentService estService;

    @Override
    public DepartmentDTO getDepartment(Long departmentId) throws RestControllerException {
        Department department = depService.getDepartment(departmentId);
        if (department == null) {
            throw new RestControllerException(ReasonCode.NoResults, "No department found with id " + departmentId);
        }
        return mapper.toDTO(department);
    }

    @Override
    public Response addDepartmentLabelLinksManually(Long departmentId, List<Long> labelIds) throws RestControllerException {
        if (departmentId == null || labelIds == null) {
            throw new RestControllerException(ReasonCode.BadRequest, "Missing input department id");
        }
        try{
            List<DepartmentLabelLink> departmentLabelLinks = depService.addDepartmentLabelLinks(departmentId, labelIds);
            return Response.status(Response.Status.OK).entity(departmentLabelLinks).build();
        } catch (NoResultException e) {
            throw new RestControllerException(ReasonCode. NoResults, e.getMessage());
        }
    }

    @Override
    public Response addDepartmentLabelLinksAutomatically(Long departmentId) throws RestControllerException {
        if (departmentId == null) {
            throw new RestControllerException(ReasonCode.BadRequest, "Missing input department id");
        }
        try{
            List<DepartmentLabelLink> departmentLabelLinks = depService.addDepartmentLabelLinksAutomatically(departmentId);
            return Response.status(Response.Status.OK).entity(departmentLabelLinks).build();
        } catch (NoResultException e) {
            throw new RestControllerException(ReasonCode. NoResults, e.getMessage());
        }
    }

    @Override
    public Response removeDepartmentLabelLink(Long departmentId, Long labelId) throws RestControllerException {

        DepartmentLabelLinkId id = new DepartmentLabelLinkId(departmentId, labelId);

        if (depService.getDepartmentLabelLink(id) == null) {
            throw new RestControllerException(ReasonCode.NoResults, "No such DepartmentLabel found");
        }

        depService.deleteDepartmentLabelLink(id);
        boolean fallbackAdded = depService.addFallbackLabelIfNeeded(departmentId);

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
    public Response createDepartmentAndDepLabelLinks(CreateDepartmentRequest request) throws RestControllerException {
        String name = request.getName();
        Long establishmentId = request.getEstablishmentId();

        if (name == null || establishmentId == null) {
            throw new RestControllerException(ReasonCode.BadRequest, "Request must have a name and departmentId");
        }

        if (estService.getEstablishment(establishmentId) == null) {
            throw new RestControllerException(ReasonCode.NoResults, "Cannot create department: establishment not found");
        }

        try{
            Department department = depService.createDepartment(name, establishmentId);
            List<DepartmentLabelLink> departmentLabelLinks = depService.addDepartmentLabelLinksAutomatically(department.getDepartmentId());
            CreateDepartmentResponse response = new CreateDepartmentResponse(department, departmentLabelLinks);

            return Response.status(Response.Status.OK).entity(response).build();
        } catch (IllegalArgumentException e) {
            throw new RestControllerException(ReasonCode.DuplicateResults, e.getMessage());
        }
    }

    @Override
    public Response deleteDepartmentAndDepLabelLinks(Long departmentId) throws RestControllerException {
        if (departmentId == null) {
            throw new RestControllerException(ReasonCode.BadRequest, "Missing input department id");
        }

        if (depService.getDepartment(departmentId) == null) {
            throw new RestControllerException(ReasonCode.NoResults, "No such Department found");
        }

        depService.deleteDepartment(departmentId);

        return Response.ok()
                .entity("{\"message\":\"Department and associated DepartmentLabels removed successfully\"}")
                .build();
    }
}

