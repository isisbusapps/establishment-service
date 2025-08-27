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
    public void create(Department department) {

    }

    @Override
    public void update(Long depId, Department department) {

    }

    @Override
    public void delete(Long depId) {

    }
}
