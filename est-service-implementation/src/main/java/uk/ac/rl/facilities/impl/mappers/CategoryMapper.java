package uk.ac.rl.facilities.impl.mappers;

import org.mapstruct.Mapper;
import uk.ac.rl.facilities.impl.config.MappingConfig;
import uk.ac.rl.facilities.impl.domains.establishment.Category;
import uk.rl.ac.facilities.api.domains.establishment.CategoryModel;

import java.util.List;

@Mapper(config = MappingConfig.class)
public abstract class CategoryMapper {

    public abstract CategoryModel toModel(Category entity);
    public abstract List<CategoryModel> toModel(List<Category> entity);

    public abstract Category toEntity(CategoryModel model);
    public abstract List<Category> toEntity(List<CategoryModel> model);
}

