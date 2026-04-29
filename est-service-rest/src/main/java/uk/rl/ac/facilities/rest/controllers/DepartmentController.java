package uk.rl.ac.facilities.rest.controllers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uk.rl.ac.facilities.api.controllers.DepartmentControllerInterface;
import uk.rl.ac.facilities.api.domains.department.DepartmentModel;
import uk.rl.ac.facilities.api.domains.department.DepartmentService;
import uk.rl.ac.facilities.api.domains.establishment.EstablishmentService;
import uk.rl.ac.facilities.api.dto.CreateDepartmentDTO;
import uk.rl.ac.facilities.api.dto.DepartmentDTO;
import uk.rl.ac.facilities.rest.mappers.DepartmentMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Transactional
public class DepartmentController implements DepartmentControllerInterface {
    @Inject
    DepartmentMapper departmentMapper;
    @Inject
    DepartmentService depService;
    @Inject
    EstablishmentService estService;

    @Override
    public DepartmentDTO getDepartment(Long departmentId) {
        DepartmentModel department = depService.getDepartment(departmentId);
        if (department == null) {
            throw new NotFoundException(
                Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("message", "No department found with id " + departmentId))
                        .type(MediaType.APPLICATION_JSON)
                        .build()
            );
        }
        return departmentMapper.toDTO(department);
    }

    @Override
    public List<DepartmentDTO> getDepartmentsByIds(List<Long> departmentIds) {

        if (departmentIds == null || departmentIds.isEmpty()) {
            return List.of();
        }

        return depService.getDepartmentsByIds(departmentIds.stream()
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList())
                .stream()
                .map(departmentMapper::toDTO)
                .toList();
    }

    @Override
    @RolesAllowed("USER_OFFICE")
    public DepartmentDTO addDepartmentLabelLinks(Long departmentId, List<Long> LabelIds) {
        if (departmentId == null) {
            throw new BadRequestException(
                Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("message", "Missing input: department id"))
                        .type(MediaType.APPLICATION_JSON)
                        .build()
            );
        }
        if (LabelIds == null || LabelIds.isEmpty()) {
            DepartmentModel departmentModel = depService.addDepartmentLabelLinksAutomatically(departmentId);
            return departmentMapper.toDTO(departmentModel);
        }
        DepartmentModel departmentModel = depService.addDepartmentLabelLinks(departmentId, LabelIds);
        return departmentMapper.toDTO(departmentModel);
    }

    @Override
    @RolesAllowed("USER_OFFICE")
    public DepartmentDTO removeDepartmentLabelLink(Long departmentId, Long labelId) {
        DepartmentModel departmentModel = depService.deleteDepartmentLabel(departmentId, labelId);

        return departmentMapper.toDTO(departmentModel);
    }

    @Override
    @RolesAllowed("USER_OFFICE")
    public DepartmentDTO removeDepartmentLabelLink(Long departmentId) {
        DepartmentModel departmentModel = depService.deleteDepartmentLabel(departmentId);

        return departmentMapper.toDTO(departmentModel);
    }

    @Override
    public DepartmentDTO createDepartment(CreateDepartmentDTO createDepartmentDTO) {
        String name = createDepartmentDTO.getName();
        Long establishmentId = createDepartmentDTO.getEstablishmentId();
        if (name == null || name.isEmpty() || establishmentId == null) {
            throw new BadRequestException(
                Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("message", "Request must have a name and establishmentId"))
                        .type(MediaType.APPLICATION_JSON)
                        .build()
            );
        }
        if (estService.getEstablishment(establishmentId) == null) {
            throw new NotFoundException(
                Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("message", "Cannot create department: establishment not found"))
                        .type(MediaType.APPLICATION_JSON)
                        .build()
            );
        }
        DepartmentModel department = depService.createDepartment(name, establishmentId);
        department = depService.addDepartmentLabelLinksAutomatically(department.getId());
        return departmentMapper.toDTO(department);
    }

    @Override
    @RolesAllowed("USER_OFFICE")
    public void deleteDepartment(Long departmentId) {
        if (departmentId == null) {
            throw new BadRequestException(
                Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("message", "Missing input department id"))
                        .type(MediaType.APPLICATION_JSON)
                        .build()
            );
        }
        if (depService.getDepartment(departmentId) == null) {
            throw new NotFoundException(
                Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("message", "No such department found"))
                        .type(MediaType.APPLICATION_JSON)
                        .build()
            );
        }
        depService.deleteDepartment(departmentId);
    }
}