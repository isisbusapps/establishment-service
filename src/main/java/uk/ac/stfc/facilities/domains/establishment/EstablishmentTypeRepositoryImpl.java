package uk.ac.stfc.facilities.domains.establishment;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class EstablishmentTypeRepositoryImpl implements EstablishmentTypeRepository {
    @Override
    public List<EstablishmentType> getTypesFromEstablishment(Long establishmentId) {
        return find("establishmentId", establishmentId).list();
    }
}
