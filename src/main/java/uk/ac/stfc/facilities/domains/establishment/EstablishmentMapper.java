package uk.ac.stfc.facilities.domains.establishment;

import com.stfc.UserOffice.BaseClasses.DateConverter;
import com.stfc.UserOffice.config.MappingConfig;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public abstract class EstablishmentMapper extends DateConverter {

    abstract EstablishmentDTO toDTO(Establishment model);

    abstract Establishment toModel(EstablishmentDTO model);
}
