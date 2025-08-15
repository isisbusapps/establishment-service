package uk.ac.stfc.facilities.domains.establishment;

import java.util.List;

public interface EstablishmentRepository {

    Establishment findById(Long estId);
    List<Establishment> getAll();
    List<EstablishmentAndAddress> findByQuery(String searchQuery);
    List<Establishment> getVerified();
    List<Establishment> getUnverified();

    void create(Establishment model);
    void update(Long estId, Establishment model);
    void delete(Long estId);
}
