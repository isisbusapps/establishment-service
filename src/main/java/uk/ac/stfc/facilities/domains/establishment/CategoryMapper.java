package uk.ac.stfc.facilities.domains.establishment;

import org.mapstruct.Mapper;
import uk.ac.stfc.facilities.config.MappingConfig;

@Mapper(config = MappingConfig.class)
public abstract class CategoryMapper {

    public abstract CategoryDTO toDTO(Category entity);

    public abstract Category toEntity(CategoryDTO dto);
}

