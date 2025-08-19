package uk.ac.stfc.facilities.controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.QueryParam;
import uk.ac.stfc.facilities.domains.establishment.EstablishmentDTO;
import uk.ac.stfc.facilities.domains.establishment.EstablishmentMapper;
import uk.ac.stfc.facilities.domains.establishment.EstablishmentService;
import java.util.List;

public class EstablishmentController implements EstablishmentControllerInterface {
    @Inject
    EstablishmentMapper mapper;
    @Inject
    EstablishmentService service;

    @Override
    public List<EstablishmentDTO> getTopEstablishmentsByQuery(String searchQuery, boolean useAliases, boolean onlyVerified, int limit) {
        return service.getTopEstablishmentsByQuery(searchQuery, useAliases, onlyVerified, limit)
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
}
