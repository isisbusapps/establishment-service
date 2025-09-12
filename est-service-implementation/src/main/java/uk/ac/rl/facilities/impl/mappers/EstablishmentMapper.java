package uk.ac.rl.facilities.impl.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.ac.rl.facilities.impl.config.DateConverter;
import uk.ac.rl.facilities.impl.config.MappingConfig;
import uk.ac.rl.facilities.impl.domains.establishment.Establishment;
import uk.rl.ac.facilities.api.domains.establishment.EstablishmentModel;

import java.util.List;

@Mapper(config = MappingConfig.class)
public abstract class EstablishmentMapper extends DateConverter {

    @Mapping(source = "establishmentName", target = "name")
    @Mapping(source = "establishmentId", target = "id")
    @Mapping(source = "rorId", target = "rorID")
    @Mapping(source = "countryName", target = "country")
    @Mapping(source = "establishmentUrl", target = "url")
    public abstract EstablishmentModel toModel(Establishment entity);

    public abstract List<EstablishmentModel> toModel(List<Establishment> entity);

    @Mapping(source = "name", target = "establishmentName")
    @Mapping(source = "id", target = "establishmentId")
    @Mapping(source = "rorID", target = "rorId")
    @Mapping(source = "country", target = "countryName")
    @Mapping(source = "url", target = "establishmentUrl")
    public abstract Establishment toEntity(EstablishmentModel model);

    public abstract List<Establishment> toEntity(List<EstablishmentModel> model);
}
