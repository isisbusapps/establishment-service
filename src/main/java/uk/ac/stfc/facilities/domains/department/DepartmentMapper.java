package uk.ac.stfc.facilities.domains.department;

import uk.ac.stfc.facilities.BaseClasses.DateConverter;
import uk.ac.stfc.facilities.config.MappingConfig;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public abstract class DepartmentMapper extends DateConverter {

    public abstract DepartmentDTO toDTO(Department entity);

    public abstract Department toEntity(DepartmentDTO dto);
}