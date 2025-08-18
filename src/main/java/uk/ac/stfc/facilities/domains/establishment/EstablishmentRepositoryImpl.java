package uk.ac.stfc.facilities.domains.establishment;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class EstablishmentRepositoryImpl implements EstablishmentRepository, PanacheRepository<Establishment> {

    @Override
    public Establishment findById(Long estId) {
        return PanacheRepository.super.findById(estId); // delegate to Panache
    }

    @Override
    public List<Establishment> getAll() {
        return List.of();
    }

    @Override
    public List<Establishment> getVerified() {
        return List.of();
    }

    @Override
    public List<Establishment> getUnverified() {
        return List.of();
    }

    @Override
    public void create(Establishment model) {

    }

    @Override
    public void update(Long estId, Establishment model) {

    }

    @Override
    public void delete(Long estId) {

    }
}
