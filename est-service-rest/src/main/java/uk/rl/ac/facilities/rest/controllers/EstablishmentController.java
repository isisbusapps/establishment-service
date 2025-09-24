package uk.rl.ac.facilities.rest.controllers;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import uk.rl.ac.facilities.api.controllers.EstablishmentControllerInterface;
import uk.rl.ac.facilities.api.domains.department.DepartmentModel;
import uk.rl.ac.facilities.api.domains.department.DepartmentService;
import uk.rl.ac.facilities.api.domains.establishment.EstablishmentModel;
import uk.rl.ac.facilities.api.domains.establishment.EstablishmentService;
import uk.rl.ac.facilities.api.dto.CountryDTO;
import uk.rl.ac.facilities.api.dto.CreateEstDTO;
import uk.rl.ac.facilities.api.dto.EstSearchQueryDTO;
import uk.rl.ac.facilities.api.dto.EstablishmentDTO;
import uk.rl.ac.facilities.facilities.api.generated.ror.RorSchemaV21;
import uk.rl.ac.facilities.rest.mappers.CountryMapper;
import uk.rl.ac.facilities.rest.mappers.EstablishmentMapper;

import java.util.List;
import java.util.Objects;

@Transactional
public class EstablishmentController implements EstablishmentControllerInterface {
    @Inject
    EstablishmentMapper estMapper;
    @Inject
    CountryMapper countryMapper;
    @Inject
    EstablishmentService estService;
    @Inject
    DepartmentService depService;

    @Override
    public EstablishmentDTO getEstablishment(Long establishmentId) {
        EstablishmentModel establishment = estService.getEstablishment(establishmentId);
        if (establishment == null) {
            throw new RuntimeException("No establishment found with id " + establishmentId);
        }
        return estMapper.toDTO(establishment);
    }

    @Override
    public List<EstablishmentDTO> getEstablishmentsByQuery(EstSearchQueryDTO searchQuery, Boolean useAliases, Boolean onlyVerified, int limit) {
        if (searchQuery == null) {
            throw new BadRequestException("Missing search query");
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
        if (createEstDTO == null || createEstDTO.getEstName().isEmpty()) {
            throw new BadRequestException("Establishment name must not be null or empty");
        }
        EstablishmentDTO newEstablishment = estMapper.toDTO(estService.createUnverifiedEstablishment(createEstDTO.getEstName()));
        return Response.status(Response.Status.CREATED).entity(newEstablishment).build();
    }

    @Override
    public List<RorSchemaV21> getRorMatches(String searchQuery) {
        if (searchQuery == null) {
            throw new BadRequestException("Missing search query");
        }

        try {
            return  estService.getRorMatches(searchQuery);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch establishments");
        }
    }

    @Override
    public EstablishmentDTO rorVerifyAndEnrichData(Long establishmentId, RorSchemaV21 rorMatch) {
        if (establishmentId == null || rorMatch == null) {
            throw new NotFoundException("Missing required input data");
        }
        try {
            estService.addRorDataToEstablishment(establishmentId, rorMatch);

            estService.addEstablishmentAliasesFromRor(establishmentId, rorMatch);

            estService.addEstablishmentCategoryLinksFromRor(establishmentId, rorMatch);

            return estMapper.toDTO(estService.getEstablishment(establishmentId));

        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Response manualVerifyAndEnrichData(Long establishmentId, EstablishmentDTO inputEst) {
        if (establishmentId == null || inputEst == null || inputEst.getName().isEmpty()) {
            throw new BadRequestException("Missing required input data");
        }
        inputEst.setId(establishmentId);
        inputEst.setVerified(true);

        estService.updateEstablishment(establishmentId, estMapper.toModel(inputEst));

        return Response.status(Response.Status.OK).entity(inputEst).build();
    }

    @Override
    public EstablishmentDTO addEstablishmentAliases(Long establishmentId, List<String> aliasNames) {
        if (establishmentId == null || aliasNames == null || aliasNames.isEmpty()) {
            throw new BadRequestException("Missing required input data");
        }
        estService.addEstablishmentAliases(establishmentId, aliasNames);
        return estMapper.toDTO(estService.getEstablishment(establishmentId));
    }

    @Override
    public EstablishmentDTO addEstablishmentCategoryLinks(Long establishmentId, List<Long> categoryIds) {
        if (establishmentId == null || categoryIds == null || categoryIds.isEmpty()) {
            throw new BadRequestException("Missing required input data");
        }
        estService.addEstablishmentCategoryLinks(establishmentId, categoryIds);
        return estMapper.toDTO(estService.getEstablishment(establishmentId));
    }

    @Override
    public Response deleteEstablishmentAliases(Long establishmentId) {
        return Response.status(501).build();
    }

    @Override
    public Response deleteEstablishmentCategoryLinks(Long establishmentId) {
        return Response.status(501).build();
    }

    @Override
    public void deleteEstablishmentAndLinkedDepartments(Long establishmentId) {
        if (establishmentId == null) {
            throw new BadRequestException("Missing input establishment id");
        }

        if (estService.getEstablishment(establishmentId) == null) {
            throw new NotFoundException("Establishment not found");
        }

        List<DepartmentModel> linkedDepartments = depService.getDepartmentsByEstablishmentId(establishmentId);

        for  (DepartmentModel dep : linkedDepartments) {
            depService.deleteDepartment(dep.getId());
        }

        estService.deleteEstablishment(establishmentId);
    }

    @Override
    public List<CountryDTO> getCountries() {
        return estService.getAllCountries()
                .stream()
                .map(countryMapper::toDTO)
                .toList();
    }
}
