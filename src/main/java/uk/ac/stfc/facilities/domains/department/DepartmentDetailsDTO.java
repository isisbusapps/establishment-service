package uk.ac.stfc.facilities.domains.department;

import java.util.List;

public class DepartmentDetailsDTO {
    private Department department;
    private List<Label> labels;


    public DepartmentDetailsDTO() {}

    public DepartmentDetailsDTO(Department department, List<Label> labels) {
        this.department = department;
        this.labels = labels;
    }

    public Department getDepartment() {return department;}

    public void setDepartment(Department department) {this.department = department;}

    public List<Label> getLabels() {return labels;}

    public void setLabels(List<Label> labels) {this.labels = labels;}
}
