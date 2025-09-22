package uk.ac.rl.facilities.impl.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.ac.rl.facilities.impl.config.MappingConfig;
import uk.ac.rl.facilities.impl.domains.department.Department;
import uk.ac.rl.facilities.impl.domains.department.DepartmentLabelLink;
import uk.ac.rl.facilities.impl.domains.department.Label;
import uk.rl.ac.facilities.api.domains.department.LabelModel;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Mapper(config = MappingConfig.class)
public abstract class LabelMapper {

    @Mapping(source = "labelId", target = "id")
    public abstract LabelModel toModel(Label entity);

    public abstract List<LabelModel> toModel(List<Label> entity);

    public List<Long> departmentLinkToDepartmentId(Set<DepartmentLabelLink> departmentLabelLinks) {
        if  (departmentLabelLinks == null) {
            return Collections.emptyList();
        }
        return departmentLabelLinks.stream().map(DepartmentLabelLink::getDepartment).map(Department::getDepartmentId).toList();
    }

    public abstract Label toEntity(LabelModel model);

    public abstract List<Label> toEntity(List<LabelModel> model);
}

