package uk.ac.stfc.facilities.helpers;

import uk.ac.stfc.facilities.domains.department.Department;
import uk.ac.stfc.facilities.domains.department.DepartmentLabel;

import java.util.List;

public class CreateDepartmentResponse {
    private Department department;
    private List<DepartmentLabel> departmentLabels;


    public CreateDepartmentResponse() {}

    public CreateDepartmentResponse(Department department, List<DepartmentLabel> departmentLabels) {
        this.department = department;
        this.departmentLabels = departmentLabels;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<DepartmentLabel> getDepartmentLabels() {
        return departmentLabels;
    }

    public void setDepartmentLabels(List<DepartmentLabel> departmentLabels) {
        this.departmentLabels = departmentLabels;
    }
}
