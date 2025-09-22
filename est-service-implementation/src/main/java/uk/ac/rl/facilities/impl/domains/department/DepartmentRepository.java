package uk.ac.rl.facilities.impl.domains.department;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

public interface DepartmentRepository extends PanacheRepository<Department> {

    List<Department> getAll();

    Department findByNameAndEstablishmentId(String name, Long establishmentId);

    void create(Department department);
    void update(Long depId, Department department);
    void delete(Long depId);
}
