package uk.rl.ac.facilities.rest.controllers;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uk.rl.ac.facilities.api.controllers.CountryControllerInterface;
import uk.rl.ac.facilities.api.domains.establishment.CountryModel;
import uk.rl.ac.facilities.api.domains.establishment.EstablishmentService;
import uk.rl.ac.facilities.api.dto.CountryDTO;
import uk.rl.ac.facilities.rest.mappers.CountryMapper;

import java.util.List;
import java.util.Map;

@Transactional
public class CountryController implements CountryControllerInterface {
    @Inject
    CountryMapper countryMapper;
    @Inject
    EstablishmentService estService;

    @Override
    public List<CountryDTO> getCountries() {
        return countryMapper.toDTO(estService.getAllCountries());
    }

    @Override
    public CountryDTO getCountry(Long countryId) {
        CountryModel country = estService.getCountry(countryId);
        if (country == null) {
            throw new NotFoundException(
                    Response.status(Response.Status.NOT_FOUND)
                            .entity(Map.of("message", "No country found with id " + countryId))
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
        }
        return countryMapper.toDTO(country);
    }
}