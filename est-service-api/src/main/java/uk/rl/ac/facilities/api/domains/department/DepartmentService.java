package uk.rl.ac.facilities.api.domains.department;

import java.util.List;

public interface DepartmentService {

    DepartmentModel createDepartment(String name, Long establishmentId);

    List<DepartmentModel> getAllDepartments();

    DepartmentModel getDepartment(Long departmentId);

    List<DepartmentModel> getDepartmentsByEstablishmentId(Long establishmentId);

    void deleteDepartment(Long depId);

    DepartmentModel addDepartmentLabelLinksAutomatically(Long departmentId);

    DepartmentModel addDepartmentLabelLinks(Long departmentId, List<Long> LabelIds);

    DepartmentModel deleteDepartmentLabel(Long departmentId, Long labelId);

    DepartmentModel deleteDepartmentLabel(Long departmentId);

    DepartmentModel addFallbackLabelIfNeeded(Long departmentId);

    List<LabelModel> getLabelsForDepartment(Long departmentId);

}
