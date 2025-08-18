package uk.ac.stfc.facilities.domains.establishment;

import uk.ac.stfc.facilities.BaseClasses.DateConverter;
import uk.ac.stfc.facilities.config.MappingConfig;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public abstract class EstablishmentMapper extends DateConverter {

    public abstract EstablishmentDTO toDTO(Establishment entity);

    public abstract Establishment toEntity(EstablishmentDTO dto);
}
