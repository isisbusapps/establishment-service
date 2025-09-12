package uk.ac.rl.facilities.impl.mappers;

import org.mapstruct.Mapper;
import uk.ac.rl.facilities.impl.config.MappingConfig;
import uk.ac.rl.facilities.impl.domains.establishment.Establishment;
import uk.ac.rl.facilities.impl.domains.establishment.EstablishmentAlias;
import uk.rl.ac.facilities.api.domains.establishment.EstablishmentAliasModel;
import uk.rl.ac.facilities.api.domains.establishment.EstablishmentModel;

import java.util.List;

@Mapper(config = MappingConfig.class)
public abstract class EstablishmentAliasMapper {

    public abstract EstablishmentAliasModel toModel(EstablishmentAlias entity);
    public abstract List<EstablishmentAliasModel> toModel(List<EstablishmentAlias> entity);

    public abstract EstablishmentAlias toEntity(EstablishmentAlias model);
    public abstract List<EstablishmentAlias> toEntity(List<EstablishmentAlias> model);
}
