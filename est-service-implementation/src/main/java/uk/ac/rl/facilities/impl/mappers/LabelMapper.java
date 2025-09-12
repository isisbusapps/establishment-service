package uk.ac.rl.facilities.impl.mappers;

import org.mapstruct.Mapper;
import uk.ac.rl.facilities.impl.config.MappingConfig;
import uk.ac.rl.facilities.impl.domains.department.Label;
import uk.rl.ac.facilities.api.domains.department.LabelModel;

import java.util.List;

@Mapper(config = MappingConfig.class)
public abstract class LabelMapper {

    public abstract LabelModel toModel(Label entity);

    public abstract List<LabelModel> toModel(List<Label> entity);

    public abstract Label toEntity(LabelModel model);

    public abstract List<Label> toEntity(List<LabelModel> model);
}

