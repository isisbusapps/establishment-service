package uk.ac.rl.facilities.impl.mappers;

import jakarta.inject.Inject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.ac.rl.facilities.impl.config.DateConverter;
import uk.ac.rl.facilities.impl.config.MappingConfig;
import uk.ac.rl.facilities.impl.domains.department.Department;
import uk.ac.rl.facilities.impl.domains.department.DepartmentLabelLink;
import uk.rl.ac.facilities.api.domains.department.DepartmentModel;
import uk.rl.ac.facilities.api.domains.department.LabelModel;

import java.util.List;
import java.util.Set;

@Mapper(config = MappingConfig.class)
public abstract class DepartmentMapper extends DateConverter {

    @Inject
    protected LabelMapper labelMapper;

    @Mapping(source = "departmentId", target = "id")
    @Mapping(source = "departmentName", target = "name")
    @Mapping(source = "oldEstablishmentId", target = "oldEstId")
    @Mapping(source = "establishmentId", target = "estId")
    @Mapping(source = "departmentLabelLinks", target = "labels")
    public abstract DepartmentModel toModel(Department entity);

    public abstract List<DepartmentModel> toModel(List<Department> entity);

    public abstract Department toEntity(DepartmentModel model);

    public abstract List<Department> toEntity(List<DepartmentModel> model);

    public List<LabelModel> toLabel(Set<DepartmentLabelLink> labelLink) {
        if  (labelLink == null) {
            return null;
        }
        return labelLink.stream().map(DepartmentLabelLink::getLabel).map(labelMapper::toModel).toList();
    }
}