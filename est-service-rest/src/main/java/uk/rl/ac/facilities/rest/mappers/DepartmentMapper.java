package uk.rl.ac.facilities.rest.mappers;

import org.mapstruct.Mapper;
import uk.rl.ac.facilities.BaseClasses.DateConverter;
import uk.rl.ac.facilities.api.domains.department.DepartmentModel;
import uk.rl.ac.facilities.api.dto.DepartmentDTO;
import uk.rl.ac.facilities.config.MappingConfig;

@Mapper(config = MappingConfig.class)
public abstract class DepartmentMapper extends DateConverter {

    public abstract DepartmentDTO toDTO(DepartmentModel model);

    public abstract DepartmentModel toModel(DepartmentDTO dto);
}