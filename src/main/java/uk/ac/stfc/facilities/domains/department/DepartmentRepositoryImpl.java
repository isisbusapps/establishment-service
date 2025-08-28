package uk.ac.stfc.facilities.domains.department;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class DepartmentRepositoryImpl implements DepartmentRepository {

    @Override
    public List<Department> getAll() {
        return listAll();
    }

    @Override
    public Department findByNameAndEstablishmentId(String name, Long establishmentId) {
        return find("name = ?1 and establishmentId = ?2", name, establishmentId).firstResult();
    }

    @Override
    public void create(Department department) {

    }

    @Override
    public void update(Long depId, Department department) {

    }

    @Override
    public void delete(Long depId) {

    }
}
