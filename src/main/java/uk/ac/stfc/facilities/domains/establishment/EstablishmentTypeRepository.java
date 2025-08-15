package uk.ac.stfc.facilities.domains.establishment;

import java.util.List;

public interface EstablishmentTypeRepository {
    List<EstablishmentType> getTypesFromEstablishmentId(Long establishmentId);
    void add(List<EstablishmentType> types);
}