package uk.ac.stfc.facilities.helpers;

import uk.ac.stfc.facilities.domains.department.Department;
import uk.ac.stfc.facilities.domains.department.DepartmentLabelLink;

import java.util.List;

public class CreateDepartmentResponse {
    private Department department;
    private List<DepartmentLabelLink> departmentLabelLinks;


    public CreateDepartmentResponse() {}

    public CreateDepartmentResponse(Department department, List<DepartmentLabelLink> departmentLabelLinks) {
        this.department = department;
        this.departmentLabelLinks = departmentLabelLinks;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<DepartmentLabelLink> getDepartmentLabels() {
        return departmentLabelLinks;
    }

    public void setDepartmentLabels(List<DepartmentLabelLink> departmentLabelLinks) {
        this.departmentLabelLinks = departmentLabelLinks;
    }
}
