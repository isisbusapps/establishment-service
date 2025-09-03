package uk.ac.stfc.facilities.domains.department;

import jakarta.persistence.NoResultException;

import java.util.List;

public interface DepartmentService {

    Department createDepartment(String name, Long establishmentId);

    List<Department> getAllDepartments();

    Department getDepartment(Long departmentId);

    List<Department> getDepartmentsByEstablishmentId(Long establishmentId);

    void deleteDepartment(Long depId) throws NoResultException;

    List<DepartmentLabelLink> addDepartmentLabelLinksAutomatically(Long departmentId);

    List<DepartmentLabelLink> addDepartmentLabelLinks(Long departmentId, List<Long> LabelIds);

    void deleteDepartmentLabelLink(DepartmentLabelLinkId id);

    boolean addFallbackLabelIfNeeded(Long departmentId);

    DepartmentLabelLink getDepartmentLabelLink(DepartmentLabelLinkId id);

    List<Label> getLabelsForDepartment(Long departmentId);

}
