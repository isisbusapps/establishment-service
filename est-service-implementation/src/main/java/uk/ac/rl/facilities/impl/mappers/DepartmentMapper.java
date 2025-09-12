package uk.ac.rl.facilities.impl.mappers;

import org.mapstruct.Mapper;
import uk.ac.rl.facilities.impl.config.DateConverter;
import uk.ac.rl.facilities.impl.config.MappingConfig;
import uk.ac.rl.facilities.impl.domains.department.Department;
import uk.rl.ac.facilities.api.domains.department.DepartmentModel;

import java.util.List;

@Mapper(config = MappingConfig.class)
public abstract class DepartmentMapper extends DateConverter {

    public abstract DepartmentModel toModel(Department entity);

    public abstract List<DepartmentModel> toModel(List<Department> entity);

    public abstract Department toEntity(DepartmentModel model);

    public abstract List<Department> toEntity(List<DepartmentModel> model);
}