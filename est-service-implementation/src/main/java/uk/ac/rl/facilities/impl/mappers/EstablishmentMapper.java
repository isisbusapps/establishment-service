package uk.ac.rl.facilities.impl.mappers;

import jakarta.inject.Inject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.ac.rl.facilities.impl.config.DateConverter;
import uk.ac.rl.facilities.impl.config.MappingConfig;
import uk.ac.rl.facilities.impl.domains.establishment.Establishment;
import uk.ac.rl.facilities.impl.domains.establishment.EstablishmentCategoryLink;
import uk.rl.ac.facilities.api.domains.establishment.CategoryModel;
import uk.rl.ac.facilities.api.domains.establishment.EstablishmentModel;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Mapper(config = MappingConfig.class, uses = {CategoryMapper.class, EstablishmentAliasMapper.class})
public abstract class EstablishmentMapper extends DateConverter {

    @Inject
    protected CategoryMapper categoryMapper;

    @Mapping(source = "establishmentName", target = "name")
    @Mapping(source = "establishmentId", target = "id")
    @Mapping(source = "rorId", target = "rorID")
    @Mapping(source = "countryName", target = "country")
    @Mapping(source = "establishmentUrl", target = "url")
    @Mapping(source = "fromDate", target = "fromDate")
    @Mapping(source = "thruDate", target = "thruDate")
    @Mapping(source = "verified", target = "verified")
    @Mapping(source = "categoryLinks", target = "categories")
    public abstract EstablishmentModel toModel(Establishment entity);

    public abstract List<EstablishmentModel> toModel(List<Establishment> entity);

    @Mapping(source = "name", target = "establishmentName")
    @Mapping(source = "id", target = "establishmentId")
    @Mapping(source = "rorID", target = "rorId")
    @Mapping(source = "country", target = "countryName")
    @Mapping(source = "url", target = "establishmentUrl")
    @Mapping(source = "fromDate", target = "fromDate")
    @Mapping(source = "thruDate", target = "thruDate")
    @Mapping(source = "verified", target = "verified")
    public abstract Establishment toEntity(EstablishmentModel model);

    public abstract List<Establishment> toEntity(List<EstablishmentModel> model);

    public List<CategoryModel> linkToCategories(Set<EstablishmentCategoryLink> categoryLinks) {
        if (categoryLinks == null) {
            return  Collections.emptyList();
        }
        return categoryLinks.stream().map(EstablishmentCategoryLink::getCategory).map(categoryMapper::toModel).toList();
    }

    protected URL map(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }
        try {
            if (!url.matches("^[a-zA-Z][a-zA-Z0-9+.-]*://.*")) {
                url = "https://" + url;
            }
            return URI.create(url).toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    protected String map(URL url) {
        return url != null ? url.toString() : null;
    }
}
