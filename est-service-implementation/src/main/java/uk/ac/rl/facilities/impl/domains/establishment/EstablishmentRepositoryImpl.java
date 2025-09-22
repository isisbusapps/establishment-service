package uk.ac.rl.facilities.impl.domains.establishment;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class EstablishmentRepositoryImpl implements EstablishmentRepository {

    @Override
    public List<Establishment> getAll() {
        return listAll();
    }

    @Override
    public List<Establishment> getVerified() {
        return list("verified", true);
    }

    @Override
    public List<Establishment> getUnverified() {
        return list("verified", false);
    }

    @Override
    public void update(Long estId, Establishment establishment) {
    }

    @Override
    public void delete(Long estId) {
    }
}
