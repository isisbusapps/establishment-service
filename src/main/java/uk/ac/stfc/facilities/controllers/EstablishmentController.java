package uk.ac.stfc.facilities.controllers;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
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
    EstablishmentService service;

    @Override
    public List<EstablishmentDTO> getEstablishmentsByQuery(String searchQuery, boolean useAliases, boolean onlyVerified, int limit) throws RestControllerException {
        if (searchQuery == null) {
            throw new RestControllerException(ReasonCode.BadRequest, "No query parameter found");
        }
        return service.getEstablishmentsByQuery(searchQuery, useAliases, onlyVerified, limit)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public List<EstablishmentDTO> getUnverifiedEstablishments() {
        return service.getUnverifiedEstablishments()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public Response createUnverifiedEstablishment(String establishmentName) throws RestControllerException {
        if (establishmentName == null) {
            throw new RestControllerException(ReasonCode.BadRequest, "Establishment name must not be null or empty");
        }
        EstablishmentDTO newEstablishment = mapper.toDTO(service.createUnverifiedEstablishment(establishmentName));
        return Response.status(Response.Status.CREATED).entity(newEstablishment).build();
    }

    @Override
    public List<RorSchemaV21> getRorMatches(String searchQuery) throws RestControllerException {
        if (searchQuery == null) {
            throw new RestControllerException(ReasonCode.BadRequest, "No query parameter found");
        }

        try {
            return  service.getRorMatches(searchQuery);
        } catch (Exception e) {
            throw new RestControllerException(ReasonCode.BadGateway, "Failed to fetch establishments");
        }
    }

    @Override
    public Response rorVerifyAndEnrichData(Long establishmentId, RorSchemaV21 rorMatch) throws RestControllerException {
        if (establishmentId == null || rorMatch == null) {
            throw new RestControllerException(ReasonCode.BadRequest, "Missing required data");
        }
        try {
            Establishment estEnriched = service.addRorDataToEstablishment(establishmentId, rorMatch);
            EstablishmentDTO estEnrichedDTO = mapper.toDTO(estEnriched);

            List<EstablishmentAlias> aliases = service.addEstablishmentAliasesFromRor(establishmentId, rorMatch);
            List<EstablishmentType> types = service.addEstablishmentTypesFromRor(establishmentId, rorMatch);

            EnrichedEstablishmentResponse response = new EnrichedEstablishmentResponse(estEnrichedDTO, aliases, types);

            return Response.status(Response.Status.OK).entity(response).build();
        } catch (IllegalArgumentException e) {
            throw new RestControllerException(ReasonCode.BadRequest, e.getMessage());
        }
    }
}
