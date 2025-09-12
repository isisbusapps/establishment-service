package uk.rl.ac.facilities.rest.controllers;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import uk.rl.ac.facilities.api.controllers.EstablishmentControllerInterface;
import uk.rl.ac.facilities.api.domains.department.DepartmentModel;
import uk.rl.ac.facilities.api.domains.department.DepartmentService;
import uk.rl.ac.facilities.api.domains.establishment.EstablishmentModel;
import uk.rl.ac.facilities.api.domains.establishment.EstablishmentService;
import uk.rl.ac.facilities.api.dto.EstablishmentDTO;
import uk.rl.ac.facilities.api.dto.EstablishmentDetailsDTO;
import uk.rl.ac.facilities.facilities.api.generated.ror.RorSchemaV21;
import uk.rl.ac.facilities.rest.mappers.EstablishmentMapper;

import java.util.List;

@Transactional
public class EstablishmentController implements EstablishmentControllerInterface {
    @Inject
    EstablishmentMapper estMapper;
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
    public EstablishmentDetailsDTO getEstablishmentDetails(Long establishmentId) {
        EstablishmentModel establishment = estService.getEstablishment(establishmentId);
        if (establishment == null) {
            throw new RuntimeException("No establishment found with id " + establishmentId);
        }
        List<String> aliases = estService.getAliasesForEstablishment(establishmentId);
        List<String> categories = estService.getCategoriesForEstablishment(establishmentId);

        return new EstablishmentDetailsDTO(estMapper.toDTO(establishment), aliases, categories);
    }

    @Override
    public List<EstablishmentDTO> getEstablishmentsByQuery(String searchQuery, boolean useAliases, boolean onlyVerified, int limit) {
        if (searchQuery == null) {
            throw new RuntimeException("Missing search query");
        }
        return estService.getEstablishmentsByQuery(searchQuery, useAliases, onlyVerified, limit)
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
    public Response createUnverifiedEstablishment(String establishmentName) {
        if (establishmentName == null || establishmentName.isEmpty()) {
            throw new RuntimeException("Establishment name must not be null or empty");
        }
        EstablishmentDTO newEstablishment = estMapper.toDTO(estService.createUnverifiedEstablishment(establishmentName));
        return Response.status(Response.Status.CREATED).entity(newEstablishment).build();
    }

    @Override
    public List<RorSchemaV21> getRorMatches(String searchQuery) {
        if (searchQuery == null) {
            throw new RuntimeException("Missing search query");
        }

        try {
            return  estService.getRorMatches(searchQuery);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch establishments");
        }
    }

    @Override
    public Response rorVerifyAndEnrichData(Long establishmentId, RorSchemaV21 rorMatch) {
        if (establishmentId == null || rorMatch == null) {
            throw new RuntimeException("Missing required input data");
        }
        try {
            EstablishmentModel estEnriched = estService.addRorDataToEstablishment(establishmentId, rorMatch);

            List<String> aliases = estService.addEstablishmentAliasesFromRor(establishmentId, rorMatch);

            estService.addEstablishmentCategoryLinksFromRor(establishmentId, rorMatch);

            List<String> categories = estService.getCategoriesForEstablishment(establishmentId);

            EstablishmentDetailsDTO response = new EstablishmentDetailsDTO(estMapper.toDTO(estEnriched), aliases, categories);

            return Response.status(Response.Status.OK).entity(response).build();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Response manualVerifyAndEnrichData(Long establishmentId, EstablishmentDTO inputEst) {
        if (establishmentId == null || inputEst == null || inputEst.getEstablishmentName().isEmpty()) {
            throw new RuntimeException("Missing required input data");
        }
        inputEst.setEstablishmentId(establishmentId);
        inputEst.setVerified(true);

        estService.updateEstablishment(establishmentId, estMapper.toModel(inputEst));

        return Response.status(Response.Status.OK).entity(inputEst).build();
    }

    @Override
    public Response addEstablishmentAliases(Long establishmentId, List<String> aliasNames) {
        if (establishmentId == null || aliasNames == null || aliasNames.isEmpty()) {
            throw new RuntimeException("Missing required input data");
        }
        estService.addEstablishmentAliases(establishmentId, aliasNames);
        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response addEstablishmentCategoryLinks(Long establishmentId, List<Long> categoryIds) {
        if (establishmentId == null || categoryIds == null || categoryIds.isEmpty()) {
            throw new RuntimeException("Missing required input data");
        }
        estService.addEstablishmentCategoryLinks(establishmentId, categoryIds);
        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response deleteEstablishmentAndLinkedDepartments(Long establishmentId) {
        if (establishmentId == null) {
            throw new RuntimeException("Missing input establishment id");
        }

        if (estService.getEstablishment(establishmentId) == null) {
            throw new RuntimeException("Establishment not found");
        }

        List<DepartmentModel> linkedDepartments = depService.getDepartmentsByEstablishmentId(establishmentId);

        for  (DepartmentModel dep : linkedDepartments) {
            depService.deleteDepartment(dep.getId());
        }

        estService.deleteEstablishment(establishmentId);

        return Response.ok()
                .entity("{\"message\":\"Establishment and associated Departments removed successfully\"}")
                .build();
    }
}
