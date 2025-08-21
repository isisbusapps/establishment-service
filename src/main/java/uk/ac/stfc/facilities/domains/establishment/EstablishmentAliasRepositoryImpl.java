package uk.ac.stfc.facilities.domains.establishment;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class EstablishmentAliasRepositoryImpl implements EstablishmentAliasRepository {
    @Override
    public List<EstablishmentAlias> getAliasesFromEstablishment(Long establishmentId) {
        return find("establishmentId", establishmentId).list();
    }
}
