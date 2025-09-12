package uk.rl.ac.facilities.rest.mappers;

import org.mapstruct.Mapper;
import uk.rl.ac.facilities.BaseClasses.DateConverter;
import uk.rl.ac.facilities.api.domains.establishment.EstablishmentModel;
import uk.rl.ac.facilities.api.dto.EstablishmentDTO;
import uk.rl.ac.facilities.config.MappingConfig;

@Mapper(config = MappingConfig.class)
public abstract class EstablishmentMapper extends DateConverter {

    public abstract EstablishmentDTO toDTO(EstablishmentModel model);

    public abstract EstablishmentModel toModel(EstablishmentDTO dto);
}
