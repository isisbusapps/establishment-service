package uk.ac.rl.facilities.impl.domains.establishment;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

public interface EstablishmentAliasRepository extends PanacheRepository<EstablishmentAlias> {
    List<EstablishmentAlias> getAliasesFromEstablishment(Long establishmentId);
}
