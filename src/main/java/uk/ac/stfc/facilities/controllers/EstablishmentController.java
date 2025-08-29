package uk.ac.stfc.facilities.controllers;

import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import uk.ac.stfc.facilities.domains.department.Department;
import uk.ac.stfc.facilities.domains.department.DepartmentService;
import uk.ac.stfc.facilities.domains.establishment.*;
import uk.ac.stfc.facilities.exceptions.RestControllerException;
import uk.ac.stfc.facilities.helpers.EnrichedEstablishmentResponse;
import uk.ac.stfc.facilities.helpers.ReasonCode;
import java.util.List;

@Transactional
public class EstablishmentController implements EstablishmentControllerInterface {
    @Inject
    EstablishmentMapper mapper;
    @Inject
    EstablishmentService estService;
    @Inject
    DepartmentService depService;

    @Override
    public List<EstablishmentDTO> getEstablishmentsByQuery(String searchQuery, boolean useAliases, boolean onlyVerified, int limit) throws RestControllerException {
        if (searchQuery == null) {
            throw new RestControllerException(ReasonCode.BadRequest, "Missing search query");
        }
        return estService.getEstablishmentsByQuery(searchQuery, useAliases, onlyVerified, limit)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public List<EstablishmentDTO> getUnverifiedEstablishments() {
        return estService.getUnverifiedEstablishments()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public Response createUnverifiedEstablishment(String establishmentName) throws RestControllerException {
        if (establishmentName == null || establishmentName.isEmpty()) {
            throw new RestControllerException(ReasonCode.BadRequest, "Establishment name must not be null or empty");
        }
        EstablishmentDTO newEstablishment = mapper.toDTO(estService.createUnverifiedEstablishment(establishmentName));
        return Response.status(Response.Status.CREATED).entity(newEstablishment).build();
    }

    @Override
    public List<RorSchemaV21> getRorMatches(String searchQuery) throws RestControllerException {
        if (searchQuery == null) {
            throw new RestControllerException(ReasonCode.BadRequest, "Missing search query");
        }

        try {
            return  estService.getRorMatches(searchQuery);
        } catch (Exception e) {
            throw new RestControllerException(ReasonCode.BadGateway, "Failed to fetch establishments");
        }
    }

    @Override
    public Response rorVerifyAndEnrichData(Long establishmentId, RorSchemaV21 rorMatch) throws RestControllerException {
        if (establishmentId == null || rorMatch == null) {
            throw new RestControllerException(ReasonCode.BadRequest, "Missing required input data");
        }
        try {
            Establishment estEnriched = estService.addRorDataToEstablishment(establishmentId, rorMatch);
            EstablishmentDTO estEnrichedDTO = mapper.toDTO(estEnriched);

            List<EstablishmentAlias> aliases = estService.addEstablishmentAliasesFromRor(establishmentId, rorMatch);
            List<EstablishmentType> types = estService.addEstablishmentTypesFromRor(establishmentId, rorMatch);

            EnrichedEstablishmentResponse response = new EnrichedEstablishmentResponse(estEnrichedDTO, aliases, types);

            return Response.status(Response.Status.OK).entity(response).build();
        } catch (IllegalArgumentException e) {
            throw new RestControllerException(ReasonCode.BadRequest, e.getMessage());
        } catch (NoResultException e) {
            throw new RestControllerException(ReasonCode. NoResults, e.getMessage());
        }
    }

    @Override
    public Response manualVerifyAndEnrichData(Long establishmentId, EstablishmentDTO inputEst) throws RestControllerException {
        if (establishmentId == null || inputEst == null || inputEst.getEstablishmentName().isEmpty()) {
            throw new RestControllerException(ReasonCode.BadRequest, "Missing required input data");
        }
        inputEst.setEstablishmentId(establishmentId);
        inputEst.setVerified(true);

        try {
            estService.updateEstablishment(establishmentId, mapper.toEntity(inputEst));
        } catch (NoResultException e) {
            throw new RestControllerException(ReasonCode.NoResults, e.getMessage());
        }

        return Response.status(Response.Status.OK).entity(inputEst).build();
    }

    @Override
    public Response addEstablishmentAliases(Long establishmentId, List<String> aliasName) throws RestControllerException {
        if (establishmentId == null || aliasName == null || aliasName.isEmpty()) {
            throw new RestControllerException(ReasonCode.BadRequest, "Missing required input data");
        }
        try{
            List<EstablishmentAlias> aliases = estService.addEstablishmentAliases(establishmentId, aliasName);
            return Response.status(Response.Status.OK).entity(aliases).build();
        } catch (NoResultException e) {
            throw new RestControllerException(ReasonCode. NoResults, e.getMessage());
        }
    }

    @Override
    public Response addEstablishmentTypes(Long establishmentId, List<String> typeNames) throws RestControllerException {
        if (establishmentId == null || typeNames == null || typeNames.isEmpty()) {
            throw new RestControllerException(ReasonCode.BadRequest, "Missing required input data");
        }
        try{
            List<EstablishmentType> types = estService.addEstablishmentTypes(establishmentId, typeNames);
            return Response.status(Response.Status.OK).entity(types).build();
        } catch (NoResultException e) {
            throw new RestControllerException(ReasonCode. NoResults, e.getMessage());
        }
    }

    @Override
    public Response deleteEstablishmentAndLinkedDepartments(Long establishmentId) throws RestControllerException {
        if (establishmentId == null) {
            throw new RestControllerException(ReasonCode.BadRequest, "Missing input establishment id");
        }

        if (estService.getEstablishment(establishmentId) == null) {
            throw new RestControllerException(ReasonCode.NoResults, "No such Establishment found");
        }

        List<Department> linkedDepartments = depService.getDepartmentsByEstablishmentId(establishmentId);

        for  (Department dep : linkedDepartments) {
            depService.deleteDepartment(dep.getDepartmentId());
        }

        estService.deleteEstablishment(establishmentId);

        return Response.ok()
                .entity("{\"message\":\"Establishment and associated Departments removed successfully\"}")
                .build();
    }
}
