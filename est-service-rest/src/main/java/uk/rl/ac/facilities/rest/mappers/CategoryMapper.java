package uk.rl.ac.facilities.rest.mappers;

import org.mapstruct.Mapper;
import uk.rl.ac.facilities.BaseClasses.DateConverter;
import uk.rl.ac.facilities.api.domains.establishment.CategoryModel;
import uk.rl.ac.facilities.api.dto.CategoryDTO;
import uk.rl.ac.facilities.config.MappingConfig;

import java.util.List;

@Mapper(config = MappingConfig.class)
public abstract class CategoryMapper extends DateConverter {

    public abstract CategoryDTO toDTO(CategoryModel categoryModel);
    public abstract List<CategoryDTO> toDTO(List<CategoryModel> categoryModel);

}
