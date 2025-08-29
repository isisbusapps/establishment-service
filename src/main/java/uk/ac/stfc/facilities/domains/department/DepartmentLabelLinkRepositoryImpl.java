package uk.ac.stfc.facilities.domains.department;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class DepartmentLabelLinkRepositoryImpl implements DepartmentLabelLinkRepository {
    @Override
    public List<Label> findLabelsLinkedToDepartment(Long departmentId) {
        return list("department.departmentId", departmentId)
                .stream()
                .map(DepartmentLabelLink::getLabel)
                .toList();
    }

    @Override
    public void remove(DepartmentLabelLink departmentLabelLink) {

    }
}
