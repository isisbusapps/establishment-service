package uk.ac.stfc.facilities.domains.department;

import org.mapstruct.Mapper;
import uk.ac.stfc.facilities.config.MappingConfig;

@Mapper(config = MappingConfig.class)
public abstract class LabelMapper {

    public abstract LabelDTO toDTO(Label entity);

    public abstract Label toEntity(LabelDTO dto);
}

