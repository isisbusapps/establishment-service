package uk.ac.stfc.facilities.domains.establishment;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class EstablishmentCategoryRepositoryImpl implements EstablishmentCategoryLinkRepository {
    @Override
    public List<EstablishmentCategoryLink> getFromEstablishment(Long establishmentId) {
        return list("establishment.establishmentId", establishmentId);
    }

    @Override
    public List<EstablishmentCategoryLink> getFromCategory(Long categoryId) {
        return list("category.categoryId", categoryId);
    }
}
