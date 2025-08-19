package uk.ac.stfc.facilities.domains.department;
import java.util.List;

public interface DepartmentRepository {

    Department findById(Long depId);
    List<Department> getAll();

    void create(Department department);
    void update(Long depId, Department department);
    void delete(Long depId);
}
