package uk.ac.stfc.facilities.controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.QueryParam;
import uk.ac.stfc.facilities.domains.establishment.EstablishmentDTO;
import uk.ac.stfc.facilities.domains.establishment.EstablishmentMapper;
import uk.ac.stfc.facilities.domains.establishment.EstablishmentService;
import uk.ac.stfc.facilities.exceptions.RestControllerException;
import uk.ac.stfc.facilities.helpers.ReasonCode;

import java.util.List;

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

        try {
            return service.getEstablishmentsByQuery(searchQuery, useAliases, onlyVerified, limit)
                    .stream()
                    .map(mapper::toDTO)
                    .toList();
        } catch (Exception e) {
            throw new RestControllerException(ReasonCode.UnexpectedError, "Error processing query");
        }
    }

    @Override
    public List<EstablishmentDTO> getUnverifiedEstablishments() {

        return service.getUnverifiedEstablishments()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
}
