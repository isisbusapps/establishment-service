package uk.ac.stfc.facilities.domains.department;
import java.util.List;

public interface DepartmentLabelRepository {

    List<Label> findLabelsLinkedToDepartment(Long departmentId);
    void addLink(DepartmentLabel departmentLabel);
    void add(List<DepartmentLabel> departmentLabels);
    void remove(DepartmentLabel departmentLabel);
}
