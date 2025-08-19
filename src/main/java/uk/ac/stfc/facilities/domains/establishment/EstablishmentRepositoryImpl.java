package uk.ac.stfc.facilities.domains.establishment;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class EstablishmentRepositoryImpl implements EstablishmentRepository, PanacheRepository<Establishment> {

    @Override
    public Establishment findById(Long estId) {
        return PanacheRepository.super.findById(estId);
    }

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
    public void create(Establishment establishment) {
    }

    @Override
    public void update(Long estId, Establishment establishment) {
    }

    @Override
    public void delete(Long estId) {
    }
}
