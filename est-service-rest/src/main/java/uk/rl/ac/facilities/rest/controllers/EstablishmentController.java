package uk.rl.ac.facilities.rest.controllers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uk.rl.ac.facilities.api.controllers.EstablishmentControllerInterface;
import uk.rl.ac.facilities.api.domains.department.DepartmentModel;
import uk.rl.ac.facilities.api.domains.department.DepartmentService;
import uk.rl.ac.facilities.api.domains.establishment.EstablishmentModel;
import uk.rl.ac.facilities.api.domains.establishment.EstablishmentService;
import uk.rl.ac.facilities.api.dto.CategoryDTO;
import uk.rl.ac.facilities.api.dto.CreateEstDTO;
import uk.rl.ac.facilities.api.dto.EstSearchQueryDTO;
import uk.rl.ac.facilities.api.dto.EstablishmentDTO;
import uk.rl.ac.facilities.facilities.api.generated.ror.RorSchemaV21;
import uk.rl.ac.facilities.rest.mappers.CategoryMapper;
import uk.rl.ac.facilities.rest.mappers.EstablishmentMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static uk.rl.ac.facilities.rest.helpers.InputValidation.validateUrl;

@Transactional
public class EstablishmentController implements EstablishmentControllerInterface {
    @Inject
    EstablishmentMapper estMapper;
    @Inject
    CategoryMapper categoryMapper;
    @Inject
    EstablishmentService estService;
    @Inject
    DepartmentService depService;

    @Override
    public EstablishmentDTO getEstablishment(Long establishmentId) {
        EstablishmentModel establishment = estService.getEstablishment(establishmentId);
        if (establishment == null) {
            throw new NotFoundException(
                    Response.status(Response.Status.NOT_FOUND)
                            .entity(Map.of("message", "No establishment found with id " + establishmentId))
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
        }
        return estMapper.toDTO(establishment);
    }

    @Override
    public List<EstablishmentDTO> getEstablishmentsByIds(List<Long> establishmentIds) {

        if (establishmentIds == null || establishmentIds.isEmpty()) {
            return List.of();
        }

        return estService.getEstablishmentsByIds(establishmentIds.stream()
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList())
                .stream()
                .map(estMapper::toDTO)
                .toList();
    }

    @Override
    public List<EstablishmentDTO> getEstablishmentsByQuery(EstSearchQueryDTO searchQuery, Boolean useAliases, Boolean onlyVerified, int limit) {
        if (searchQuery == null || searchQuery.name == null) {
            throw new BadRequestException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(Map.of("message", "Missing search query"))
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
        } else if (searchQuery.name.isEmpty()) {
            return List.of();
        }
        return estService.getEstablishmentsByQuery(searchQuery.name, useAliases, onlyVerified, limit)
                .stream()
                .map(estMapper::toDTO)
                .toList();
    }

    @Override
    public List<EstablishmentDTO> getUnverifiedEstablishments() {
        return estService.getUnverifiedEstablishments()
                .stream()
                .map(estMapper::toDTO)
                .toList();
    }

    @Override
    public Response createUnverifiedEstablishment(CreateEstDTO createEstDTO) {
        if (createEstDTO == null
                || createEstDTO.getEstName() == null || createEstDTO.getEstName().isEmpty()
                || createEstDTO.getCountry() == null || createEstDTO.getCountry().isEmpty()) {
            throw new BadRequestException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(Map.of("message", "Establishment name or country must not be null or empty"))
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
        }
        try {
            validateUrl(createEstDTO.getUrl());
            EstablishmentDTO newEstablishment = estMapper.toDTO(estService.createUnverifiedEstablishment(
                    createEstDTO.getEstName(), createEstDTO.getCountry(), createEstDTO.getUrl()));
            return Response.status(Response.Status.CREATED).entity(newEstablishment).build();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(Map.of("message", e.getMessage()))
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
        }
    }

    @Override
    public List<RorSchemaV21> getRorMatches(String searchQuery) {
        if (searchQuery == null) {
            throw new BadRequestException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(Map.of("message", "Missing search query"))
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
        }
        try {
            return estService.getRorMatches(searchQuery);
        } catch (Exception e) {
            throw new InternalServerErrorException(
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(Map.of("message", "Failed to fetch establishments"))
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
        }
    }

    @Override
    @RolesAllowed("USER_OFFICE")
    public EstablishmentDTO rorVerifyAndEnrichData(Long establishmentId, RorSchemaV21 rorMatch) {
        if (establishmentId == null || rorMatch == null) {
            throw new BadRequestException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(Map.of("message", "Missing required input data"))
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
        }
        try {
            estService.addRorDataToEstablishment(establishmentId, rorMatch);

            estService.addEstablishmentAliasesFromRor(establishmentId, rorMatch);

            estService.addEstablishmentCategoryLinksFromRor(establishmentId, rorMatch);

            return estMapper.toDTO(estService.getEstablishment(establishmentId));

        } catch (IllegalArgumentException e) {
            throw new InternalServerErrorException(
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(Map.of("message", e.getMessage()))
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
        }
    }

    @Override
    @RolesAllowed("USER_OFFICE")
    public Response manualEnrichData(Long establishmentId, EstablishmentDTO inputEst) {
        if (establishmentId == null || inputEst == null || inputEst.getName() == null || inputEst.getName().isEmpty()) {
            throw new BadRequestException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(Map.of("message", "Missing required input data"))
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
        }

        validateUrl(inputEst.getUrl());
        inputEst.setId(establishmentId);

        estService.updateEstablishment(establishmentId, estMapper.toModel(inputEst));

        return Response.status(Response.Status.OK).entity(inputEst).build();
    }

    @Override
    @RolesAllowed("USER_OFFICE")
    public EstablishmentDTO addEstablishmentAliases(Long establishmentId, List<String> aliasNames) {
        if (establishmentId == null || aliasNames == null || aliasNames.isEmpty()) {
            throw new BadRequestException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(Map.of("message", "Missing required input data"))
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
        }
        estService.addEstablishmentAliases(establishmentId, aliasNames);
        return estMapper.toDTO(estService.getEstablishment(establishmentId));
    }

    @Override
    public EstablishmentDTO addEstablishmentCategoryLinks(Long establishmentId, List<Long> categoryIds) {
        if (establishmentId == null || categoryIds == null || categoryIds.isEmpty()) {
            throw new BadRequestException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(Map.of("message", "Missing required input data"))
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
        }
        estService.addEstablishmentCategoryLinks(establishmentId, categoryIds);
        return estMapper.toDTO(estService.getEstablishment(establishmentId));
    }

    @Override
    @RolesAllowed("USER_OFFICE")
    public Response deleteEstablishmentAliases(Long establishmentId) {
        return Response.status(501).build();
    }

    @Override
    @RolesAllowed("USER_OFFICE")
    public Response deleteEstablishmentCategoryLinks(Long establishmentId) {
        return Response.status(501).build();
    }

    @Override
    @RolesAllowed("USER_OFFICE")
    public void deleteEstablishmentAndLinkedDepartments(Long establishmentId) {
        if (establishmentId == null) {
            throw new BadRequestException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(Map.of("message", "Missing input establishment id"))
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
        }

        if (estService.getEstablishment(establishmentId) == null) {
            throw new NotFoundException(
                    Response.status(Response.Status.NOT_FOUND)
                            .entity(Map.of("message", "Establishment not found"))
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
        }

        List<DepartmentModel> linkedDepartments = depService.getDepartmentsByEstablishmentId(establishmentId);

        for (DepartmentModel dep : linkedDepartments) {
            depService.deleteDepartment(dep.getId());
        }

        estService.deleteEstablishment(establishmentId);
    }

    @Override
    public EstablishmentDTO getEstablishmentByRorId(String rorIdSuffix) {
        EstablishmentModel establishment = estService.getEstablishmentByRorId(rorIdSuffix);
        return estMapper.toDTO(establishment);
    }

    @Override
    public List<CategoryDTO> getEstablishmentCategories() {
        return categoryMapper.toDTO(estService.getAllCategories());
    }
}