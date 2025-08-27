package uk.ac.stfc.facilities.domains.department;

import java.util.List;

public class DepartmentLabelRepositoryImpl implements DepartmentLabelRepository {
    @Override
    public List<Label> findLabelsLinkedToDepartment(Long departmentId) {
        return List.of();
    }

    @Override
    public void addLink(DepartmentLabel departmentLabel) {

    }

    @Override
    public void add(List<DepartmentLabel> departmentLabels) {

    }

    @Override
    public void remove(DepartmentLabel departmentLabel) {

    }
}
