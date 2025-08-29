package uk.ac.stfc.facilities.domains.establishment;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

public interface EstablishmentCategoryRepository extends PanacheRepository<EstablishmentCategoryLink> {
    List<EstablishmentCategoryLink> getFromEstablishment(Long establishmentId);
}