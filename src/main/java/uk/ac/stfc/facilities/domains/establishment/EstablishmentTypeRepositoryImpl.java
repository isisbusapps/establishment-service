package uk.ac.stfc.facilities.domains.establishment;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class EstablishmentTypeRepositoryImpl implements EstablishmentTypeRepository {
    @Override
    public List<EstablishmentType> getTypesFromEstablishmentId(Long establishmentId) {
        return List.of();
    }

    @Override
    public void add(List<EstablishmentType> types) {

    }
}
