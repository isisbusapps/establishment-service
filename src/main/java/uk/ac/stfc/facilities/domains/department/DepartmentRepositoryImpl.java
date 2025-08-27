package uk.ac.stfc.facilities.domains.department;

import java.util.List;

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
