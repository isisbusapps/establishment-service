package uk.rl.ac.facilities.rest.mappers;

import org.mapstruct.Mapper;
import uk.rl.ac.facilities.api.domains.department.LabelModel;
import uk.rl.ac.facilities.api.dto.LabelDTO;
import uk.rl.ac.facilities.config.MappingConfig;

@Mapper(config = MappingConfig.class)
public abstract class LabelMapper {

    public abstract LabelDTO toDTO(LabelModel model);

    public abstract LabelModel toModel(LabelDTO dto);
}

