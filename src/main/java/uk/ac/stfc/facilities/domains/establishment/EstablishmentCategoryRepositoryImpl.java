package uk.ac.stfc.facilities.domains.establishment;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class EstablishmentCategoryRepositoryImpl implements EstablishmentCategoryRepository {
    @Override
    public List<EstablishmentCategoryLink> getFromEstablishment(Long establishmentId) {
        return find("establishmentId", establishmentId).list();
    }
}
