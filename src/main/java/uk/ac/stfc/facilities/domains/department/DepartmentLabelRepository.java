package uk.ac.stfc.facilities.domains.department;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

public interface DepartmentLabelRepository extends PanacheRepository<DepartmentLabel> {

    List<Label> findLabelsLinkedToDepartment(Long departmentId);
    void remove(DepartmentLabel departmentLabel);
}
