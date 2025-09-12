package uk.rl.ac.facilities.rest.mappers;

import org.mapstruct.Mapper;
import uk.rl.ac.facilities.api.domains.establishment.EstablishmentAliasModel;
import uk.rl.ac.facilities.api.dto.EstablishmentAliasDTO;
import uk.rl.ac.facilities.config.MappingConfig;

@Mapper(config = MappingConfig.class)
public abstract class EstablishmentAliasMapper {

    public abstract EstablishmentAliasDTO toDTO(EstablishmentAliasModel model);

    public abstract EstablishmentAliasModel toDTO(EstablishmentAliasDTO dto);
}
