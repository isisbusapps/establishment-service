package uk.ac.stfc.facilities.domains.establishment;

import org.mapstruct.Mapper;
import uk.ac.stfc.facilities.config.MappingConfig;

@Mapper(config = MappingConfig.class)
public abstract class EstablishmentAliasMapper {

    public abstract EstablishmentAliasDTO toDTO(EstablishmentAlias entity);

    public abstract EstablishmentAlias toEntity(EstablishmentAliasDTO dto);
}
