package uk.ac.rl.facilities.impl.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.ac.rl.facilities.impl.config.MappingConfig;
import uk.ac.rl.facilities.impl.domains.establishment.Establishment;
import uk.ac.rl.facilities.impl.domains.establishment.EstablishmentAlias;
import uk.rl.ac.facilities.api.domains.establishment.AliasModel;

import java.util.List;

@Mapper(config = MappingConfig.class, uses = EstablishmentMapper.class)
public abstract class EstablishmentAliasMapper {

    @Mapping(source = "aliasId", target = "id")
    @Mapping(source = "establishment", target = "establishmentId")
    public abstract AliasModel toModel(EstablishmentAlias entity);
    public abstract List<AliasModel> toModel(List<EstablishmentAlias> entity);

    public abstract EstablishmentAlias toEntity(EstablishmentAlias model);
    public abstract List<EstablishmentAlias> toEntity(List<EstablishmentAlias> model);

    public Long establishmentToEstablishmentId(Establishment entity) {
        if (entity == null) {
            return null;
        }
        return entity.getEstablishmentId();
    }
}
