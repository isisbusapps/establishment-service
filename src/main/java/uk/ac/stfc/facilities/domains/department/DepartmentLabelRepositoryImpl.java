package uk.ac.stfc.facilities.domains.department;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class DepartmentLabelRepositoryImpl implements DepartmentLabelRepository {
    @Override
    public List<Label> findLabelsLinkedToDepartment(Long departmentId) {
        return List.of();
    }

    @Override
    public void remove(DepartmentLabel departmentLabel) {

    }
}
