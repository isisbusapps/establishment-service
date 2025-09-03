package uk.ac.stfc.facilities.domains.establishment;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.List;

public interface EstablishmentCategoryLinkRepository extends PanacheRepositoryBase<EstablishmentCategoryLink, EstablishmentCategoryLinkId> {
    List<EstablishmentCategoryLink>  getFromEstablishment(Long establishmentId);
    List<EstablishmentCategoryLink>  getFromCategory(Long categoryId);
    List<Category> findCategoriesLinkedToEstablishment(Long establishmentId);
}