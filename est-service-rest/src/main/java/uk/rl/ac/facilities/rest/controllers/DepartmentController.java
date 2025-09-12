package uk.rl.ac.facilities.rest.controllers;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import uk.rl.ac.facilities.api.controllers.DepartmentControllerInterface;
import uk.rl.ac.facilities.api.domains.department.DepartmentLabelLinkIdModel;
import uk.rl.ac.facilities.api.domains.department.DepartmentModel;
import uk.rl.ac.facilities.api.domains.department.DepartmentService;
import uk.rl.ac.facilities.api.domains.establishment.EstablishmentService;
import uk.rl.ac.facilities.api.dto.CreateDepartmentDTO;
import uk.rl.ac.facilities.api.dto.DepartmentDTO;
import uk.rl.ac.facilities.api.dto.DepartmentDetailsDTO;
import uk.rl.ac.facilities.rest.mappers.DepartmentMapper;
import uk.rl.ac.facilities.rest.mappers.LabelMapper;

import java.util.List;

@Transactional
public class DepartmentController implements DepartmentControllerInterface {
    @Inject
    DepartmentMapper departmentMapper;
    @Inject
    LabelMapper labelMapper;
    @Inject
    DepartmentService depService;
    @Inject
    EstablishmentService estService;

    @Override
    public DepartmentDTO getDepartment(Long departmentId) {
        DepartmentModel department = depService.getDepartment(departmentId);
        if (department == null) {
            throw new RuntimeException("No department found with id " + departmentId);
        }
        return departmentMapper.toDTO(department);
    }

    @Override
    public DepartmentDetailsDTO getDepartmentDetails(Long departmentId) {
        DepartmentModel department = depService.getDepartment(departmentId);
        if (department == null) {
            throw new RuntimeException("No department found with id " + departmentId);
        }
        List<String> labelsDtos = depService.getLabelsForDepartment(departmentId);
        return new DepartmentDetailsDTO(departmentMapper.toDTO(department), labelsDtos);
    }

    @Override
    public Response addDepartmentLabelLinksManually(Long departmentId, List<Long> labelIds) {
        if (departmentId == null || labelIds == null) {
            throw new RuntimeException("Missing input department id");
        }
       depService.addDepartmentLabelLinks(departmentId, labelIds);
        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response addDepartmentLabelLinksAutomatically(Long departmentId) {
        if (departmentId == null) {
            throw new RuntimeException("Missing input department id");
        }
        depService.addDepartmentLabelLinksAutomatically(departmentId);
        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response removeDepartmentLabelLink(Long departmentId, Long labelId) {

        DepartmentLabelLinkIdModel id = new DepartmentLabelLinkIdModel(departmentId, labelId);

        if (depService.getDepartmentLabelLink(departmentId, labelId) == null) {
            throw new RuntimeException("No such DepartmentLabel found");
        }

        depService.deleteDepartmentLabelLink(departmentId, labelId);
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
    public Response createDepartmentAndDepLabelLinks(CreateDepartmentDTO createDepartmentDTO) {
        String name = createDepartmentDTO.getName();
        Long establishmentId = createDepartmentDTO.getEstablishmentId();

        if (name == null || establishmentId == null) {
            throw new RuntimeException("Request must have a name and departmentId");
        }

        if (estService.getEstablishment(establishmentId) == null) {
            throw new RuntimeException("Cannot create department: establishment not found");
        }

        try{
            DepartmentModel department = depService.createDepartment(name, establishmentId);
            depService.addDepartmentLabelLinksAutomatically(department.getId());
            List<String> labelDtos = depService.getLabelsForDepartment(department.getId());

            DepartmentDetailsDTO response = new DepartmentDetailsDTO(departmentMapper.toDTO(department), labelDtos);

            return Response.status(Response.Status.OK).entity(response).build();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Response deleteDepartmentAndDepLabelLinks(Long departmentId) {
        if (departmentId == null) {
            throw new RuntimeException("Missing input department id");
        }

        if (depService.getDepartment(departmentId) == null) {
            throw new RuntimeException("No such Department found");
        }

        depService.deleteDepartment(departmentId);

        return Response.ok()
                .entity("{\"message\":\"Department and associated DepartmentLabels removed successfully\"}")
                .build();
    }
}

