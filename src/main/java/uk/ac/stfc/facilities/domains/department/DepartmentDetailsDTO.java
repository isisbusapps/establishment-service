package uk.ac.stfc.facilities.domains.department;

import java.util.List;

public class DepartmentDetailsDTO {
    private Department department;
    private List<DepartmentLabelLink> departmentLabelLinks;


    public DepartmentDetailsDTO() {}

    public DepartmentDetailsDTO(Department department, List<DepartmentLabelLink> departmentLabelLinks) {
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
