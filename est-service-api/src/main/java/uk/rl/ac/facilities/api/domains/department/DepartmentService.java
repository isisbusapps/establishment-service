package uk.rl.ac.facilities.api.domains.department;

import java.util.List;

public interface DepartmentService {

    DepartmentModel createDepartment(String name, Long establishmentId);

    List<DepartmentModel> getAllDepartments();

    DepartmentModel getDepartment(Long departmentId);

    List<DepartmentModel> getDepartmentsByEstablishmentId(Long establishmentId);

    void deleteDepartment(Long depId);

    List<String> addDepartmentLabelLinksAutomatically(Long departmentId);

    List<String> addDepartmentLabelLinks(Long departmentId, List<Long> LabelIds);

    void deleteDepartmentLabelLink(Long departmentId, Long labelId);

    boolean addFallbackLabelIfNeeded(Long departmentId);

    String getDepartmentLabelLink(Long departmentId, Long labelId);

    List<String> getLabelsForDepartment(Long departmentId);

}
