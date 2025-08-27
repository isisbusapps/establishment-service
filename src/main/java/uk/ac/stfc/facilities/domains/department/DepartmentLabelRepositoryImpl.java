package uk.ac.stfc.facilities.domains.department;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class DepartmentLabelRepositoryImpl implements DepartmentLabelRepository {
    @Override
    public List<Label> findLabelsLinkedToDepartment(Long departmentId) {
        return list("department.id", departmentId)
                .stream()
                .map(DepartmentLabel::getLabel)
                .toList();
    }

    @Override
    public void remove(DepartmentLabel departmentLabel) {

    }
}
