package uk.rl.ac.facilities.api.dto;

import java.util.List;

public class DepartmentDetailsDTO {
    private DepartmentDTO departmentDto;
    private List<String> labels;

    public DepartmentDetailsDTO() {}

    public DepartmentDetailsDTO(DepartmentDTO departmentDto, List<String> labels) {
        this.departmentDto = departmentDto;
        this.labels = labels;
    }

    public DepartmentDTO getDepartmentDto() {return departmentDto;}

    public void setDepartmentDto(DepartmentDTO departmentDto) {this.departmentDto = departmentDto;}

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
}
