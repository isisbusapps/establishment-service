package uk.ac.rl.facilities.impl.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.ac.rl.facilities.impl.config.MappingConfig;
import uk.ac.rl.facilities.impl.domains.establishment.Country;
import uk.rl.ac.facilities.api.domains.establishment.CountryModel;

import java.util.List;

@Mapper(config = MappingConfig.class)
public abstract class CountryMapper {

    @Mapping(source = "countryId", target = "id")
    @Mapping(source = "countryName", target = "name")
    public abstract CountryModel toModel(Country entity);
    public abstract List<CountryModel> toModel(List<Country> entity);
    public abstract Country toEntity(CountryModel model);
    public abstract List<Country> toEntity(List<CountryModel> model);

}

