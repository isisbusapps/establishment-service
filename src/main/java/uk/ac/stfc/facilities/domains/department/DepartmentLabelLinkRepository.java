package uk.ac.stfc.facilities.domains.department;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.List;

public interface DepartmentLabelLinkRepository extends PanacheRepositoryBase<DepartmentLabelLink, DepartmentLabelLinkId> {

    List<Label> findLabelsLinkedToDepartment(Long departmentId);
    void remove(DepartmentLabelLink departmentLabelLink);
}
