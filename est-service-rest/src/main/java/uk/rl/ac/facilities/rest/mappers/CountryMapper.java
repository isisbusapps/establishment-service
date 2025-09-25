package uk.rl.ac.facilities.rest.mappers;

import org.mapstruct.Mapper;
import uk.rl.ac.facilities.BaseClasses.DateConverter;
import uk.rl.ac.facilities.api.domains.establishment.CountryModel;
import uk.rl.ac.facilities.api.dto.CountryDTO;
import uk.rl.ac.facilities.config.MappingConfig;

import java.util.List;

@Mapper(config = MappingConfig.class)
public abstract class CountryMapper extends DateConverter {

    public abstract CountryDTO toDTO(CountryModel countryModel);
    public abstract List<CountryDTO> toDTO(List<CountryModel> countryModel);

    public abstract CountryModel toModel(CountryDTO countryDTO);
    public abstract List<CountryModel> toModel(List<CountryDTO> countryDTO);

}
