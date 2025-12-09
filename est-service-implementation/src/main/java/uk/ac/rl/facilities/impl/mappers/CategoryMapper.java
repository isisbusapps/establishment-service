package uk.ac.rl.facilities.impl.mappers;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import uk.ac.rl.facilities.impl.config.MappingConfig;
import uk.ac.rl.facilities.impl.domains.establishment.Category;
import uk.ac.rl.facilities.impl.domains.establishment.EstablishmentCategoryLink;
import uk.rl.ac.facilities.api.domains.establishment.CategoryModel;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Mapper(config = MappingConfig.class)
public abstract class CategoryMapper {

    // Full mapping (with establishment IDs)
    @Mapping(source = "categoryId", target = "id")
    @Mapping(source = "categoryName", target = "name")
    @Mapping(source = "categoryLinks", target = "establishmentIDs")
    @Named("full")
    public abstract CategoryModel toModel(Category entity);

    @IterableMapping(qualifiedByName = "shallow")
    public abstract List<CategoryModel> toModel(List<Category> entity);

    public abstract Category toEntity(CategoryModel model);
    public abstract List<Category> toEntity(List<CategoryModel> model);

    public List<Long> categoryLinkToEstIDs(Set<EstablishmentCategoryLink> categoryLinks) {
        if (categoryLinks == null) {
            return Collections.emptyList();
        }
        return categoryLinks.stream().map(EstablishmentCategoryLink::getEstablishmentId).toList();
    }

    // Mapping to model ignoring establishment ids for just a list of categories
    @Mapping(source = "categoryId", target = "id")
    @Mapping(source = "categoryName", target = "name")
    @Mapping(target = "establishmentIDs", ignore = true)
    @Named("shallow")
    public abstract CategoryModel toShallowModel(Category entity);

    @IterableMapping(qualifiedByName = "shallow")
    public abstract List<CategoryModel> toShallowModel(List<Category> entities);
}

