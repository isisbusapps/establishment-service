package uk.ac.stfc.facilities.domains.department;

import jakarta.persistence.NoResultException;

import java.util.List;

public interface DepartmentService {

    Department createDepartment(Department department);

    List<Department> getAllDepartments();

    Department getDepartmentsById(Long depId);

    List<Department> getDepartmentsByEstablishmentId(Long establishmentId);

    Department deleteDepartment(Long depId) throws NoResultException;

    List<DepartmentLabel> addDepartmentLabelsAutomatically(Long departmentId);

    List<DepartmentLabel> addDepartmentLabels(Long departmentId, List<Long> LabelIds);

    boolean removeDepartmentLabel(Long departmentId, Long labelId);

}
