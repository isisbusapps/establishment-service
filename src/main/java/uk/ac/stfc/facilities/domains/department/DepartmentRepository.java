package uk.ac.stfc.facilities.domains.department;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

public interface DepartmentRepository extends PanacheRepository<Department> {

    List<Department> getAll();

    void create(Department department);
    void update(Long depId, Department department);
    void delete(Long depId);
}
