package uk.ac.rl.facilities.impl.domains.establishment;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

public interface EstablishmentRepository extends PanacheRepository<Establishment> {

    List<Establishment> getAll();
    List<Establishment> getVerified();
    List<Establishment> getUnverified();

    void update(Long estId, Establishment establishment);
    void delete(Long estId);
}
