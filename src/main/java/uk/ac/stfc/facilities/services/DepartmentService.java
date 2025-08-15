package uk.ac.stfc.facilities.services;

import jakarta.persistence.NoResultException;
import uk.ac.stfc.facilities.domains.department.Department;
import uk.ac.stfc.facilities.domains.department.DepartmentLabel;
import uk.ac.stfc.facilities.domains.department.Label;

import java.util.List;

public interface DepartmentService {

    Department createDepartment(Department department);

    List<Department> getAllDepartments();

    Department getDepartmentsById(Long depId);

    List<Department> getDepartmentsByEstablishmentId(Long establishmentId);

    Department deleteDepartment(Long depId) throws NoResultException;

    List<DepartmentLabel> addDepartmentLabelsAutomatically(Department department);

    List<DepartmentLabel> addDepartmentLabelsManually(Department department, List<Label> newLabels);

    boolean removeDepartmentLabel(Department department, Label labelToRemove);

}
