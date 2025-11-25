package uk.rl.ac.facilities.rest.controllers;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uk.rl.ac.facilities.api.controllers.CountryControllerInterface;
import uk.rl.ac.facilities.api.domains.establishment.CountryModel;
import uk.rl.ac.facilities.api.domains.establishment.EstablishmentService;
import uk.rl.ac.facilities.api.dto.CountryDTO;
import uk.rl.ac.facilities.rest.mappers.CountryMapper;

import java.util.List;

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
            throw new RuntimeException("No country found with id " + countryId);
        }
        return countryMapper.toDTO(country);
    }
}
