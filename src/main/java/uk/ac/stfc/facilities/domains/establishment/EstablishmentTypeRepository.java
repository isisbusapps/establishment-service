package uk.ac.stfc.facilities.domains.establishment;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

public interface EstablishmentTypeRepository extends PanacheRepository<EstablishmentType> {
    List<EstablishmentType> getTypesFromEstablishment(Long establishmentId);
}