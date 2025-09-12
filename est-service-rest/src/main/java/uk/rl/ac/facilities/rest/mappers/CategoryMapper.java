package uk.rl.ac.facilities.rest.mappers;

import org.mapstruct.Mapper;
import uk.rl.ac.facilities.api.domains.establishment.CategoryModel;
import uk.rl.ac.facilities.api.dto.CategoryDTO;
import uk.rl.ac.facilities.config.MappingConfig;

@Mapper(config = MappingConfig.class)
public abstract class CategoryMapper {

    public abstract CategoryDTO toDTO(CategoryModel model);

    public abstract CategoryModel toModel(CategoryDTO dto);
}

