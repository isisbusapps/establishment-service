package uk.rl.ac.facilities.rest.controllers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import uk.rl.ac.facilities.api.controllers.DepartmentControllerInterface;
import uk.rl.ac.facilities.api.domains.department.DepartmentModel;
import uk.rl.ac.facilities.api.domains.department.DepartmentService;
import uk.rl.ac.facilities.api.domains.establishment.EstablishmentService;
import uk.rl.ac.facilities.api.dto.CreateDepartmentDTO;
import uk.rl.ac.facilities.api.dto.DepartmentDTO;
import uk.rl.ac.facilities.rest.mappers.DepartmentMapper;

import java.util.List;

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
            throw new RuntimeException("No department found with id " + departmentId);
        }
        return departmentMapper.toDTO(department);
    }

    @Override
    @RolesAllowed("USER_OFFICE")
    public DepartmentDTO addDepartmentLabelLinks(Long departmentId, List<Long> LabelIds) {
        if (departmentId == null) {
            throw new BadRequestException("Missing input: department id");
        };
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

        if (name == null || establishmentId == null) {
            throw new BadRequestException("Request must have a name and establishmentId");
        }

        if (estService.getEstablishment(establishmentId) == null) {
            throw new NotFoundException("Cannot create department: establishment not found");
        }

        DepartmentModel department = depService.createDepartment(name, establishmentId);
        department = depService.addDepartmentLabelLinksAutomatically(department.getId());
        return departmentMapper.toDTO(department);
    }

    @Override
    @RolesAllowed("USER_OFFICE")
    public void deleteDepartment(Long departmentId) {
        if (departmentId == null) {
            throw new BadRequestException("Missing input department id");
        }

        if (depService.getDepartment(departmentId) == null) {
            throw new NotFoundException("No such Department found");
        }

        depService.deleteDepartment(departmentId);
        }
}

