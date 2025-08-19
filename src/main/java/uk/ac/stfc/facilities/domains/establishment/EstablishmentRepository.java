package uk.ac.stfc.facilities.domains.establishment;

import java.util.List;

public interface EstablishmentRepository {

    Establishment findById(Long estId);
    List<Establishment> getAll();
    List<Establishment> getVerified();
    List<Establishment> getUnverified();

    void create(Establishment establishment);
    void update(Long estId, Establishment establishment);
    void delete(Long estId);
}
