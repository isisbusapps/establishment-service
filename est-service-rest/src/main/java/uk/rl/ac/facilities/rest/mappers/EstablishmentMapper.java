package uk.rl.ac.facilities.rest.mappers;

import org.mapstruct.Mapper;
import uk.rl.ac.facilities.BaseClasses.DateConverter;
import uk.rl.ac.facilities.api.domains.establishment.AliasModel;
import uk.rl.ac.facilities.api.domains.establishment.CategoryModel;
import uk.rl.ac.facilities.api.domains.establishment.EstablishmentModel;
import uk.rl.ac.facilities.api.dto.EstablishmentDTO;
import uk.rl.ac.facilities.config.MappingConfig;

import java.util.Collections;
import java.util.List;

@Mapper(config = MappingConfig.class)
public abstract class EstablishmentMapper extends DateConverter {

    public abstract EstablishmentDTO toDTO(EstablishmentModel establishmentModel);
    public abstract List<EstablishmentDTO> toDTO(List<EstablishmentModel> establishmentModel);

    public abstract EstablishmentModel toModel(EstablishmentDTO establishmentDTO);
    public abstract List<EstablishmentModel> toModel(List<EstablishmentDTO> establishmentDTO);

    public List<String> categoriesToString(List<CategoryModel> categories) {
        if (categories == null || categories.isEmpty()) {
            return Collections.emptyList();
        }
        return categories.stream().map(CategoryModel::getName).toList();
    }

    public List<String> aliasesToString(List<AliasModel> aliases) {
        if (aliases == null || aliases.isEmpty()) {
            return Collections.emptyList();
        }
        return aliases.stream().map(AliasModel::getAlias).toList();
    }

    public List<CategoryModel> stringToCategories(List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            return Collections.emptyList();
        }
        return categories.stream().map(name -> {
            CategoryModel model = new CategoryModel();
            model.setName(name);
            return model;
        }).toList();
    }

    public List<AliasModel> stringToAliases(List<String> aliases) {
        if (aliases == null || aliases.isEmpty()) {
            return Collections.emptyList();
        }
        return aliases.stream().map(name -> {
            AliasModel model = new AliasModel();
            model.setAlias(name);
            return model;
        }).toList();
    }
}
