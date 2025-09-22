package uk.rl.ac.facilities.rest.mappers;

import org.mapstruct.Mapper;
import uk.rl.ac.facilities.BaseClasses.DateConverter;
import uk.rl.ac.facilities.api.domains.department.DepartmentModel;
import uk.rl.ac.facilities.api.domains.department.LabelModel;
import uk.rl.ac.facilities.api.domains.establishment.CategoryModel;
import uk.rl.ac.facilities.api.dto.DepartmentDTO;
import uk.rl.ac.facilities.config.MappingConfig;

import java.util.Collections;
import java.util.List;

@Mapper(config = MappingConfig.class)
public abstract class DepartmentMapper extends DateConverter {

    public abstract DepartmentDTO toDTO(DepartmentModel department);
    public abstract List<DepartmentDTO> toDTO(List<DepartmentModel> department);

    public abstract DepartmentModel toModel(DepartmentDTO departmentDTO);
    public abstract List<DepartmentModel> toModel(List<DepartmentDTO> departmentDTO);

    public List<String> labelsToString(List<LabelModel> labels) {
        if (labels==null || labels.isEmpty()) {
            return Collections.emptyList();
        };
        return labels.stream().map(LabelModel::getLabelName).toList();
    }

    public List<LabelModel> stringToLabels(List<String> labels) {
        if (labels == null || labels.isEmpty()) {
            return Collections.emptyList();
        }
        return labels.stream().map(name -> {
            LabelModel model = new LabelModel();
            model.setLabelName(name);
            return model;
        }).toList();
    }
}