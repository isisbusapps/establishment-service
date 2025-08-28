package uk.ac.stfc.facilities.domains.department;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.List;

public interface DepartmentLabelRepository extends PanacheRepositoryBase<DepartmentLabel, DepartmentLabelId> {

    List<Label> findLabelsLinkedToDepartment(Long departmentId);
    void remove(DepartmentLabel departmentLabel);
}
