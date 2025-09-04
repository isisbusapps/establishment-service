package uk.ac.stfc.facilities.domains.department;

import uk.ac.stfc.facilities.BaseClasses.Dto;

import java.util.List;

public class DepartmentDetailsDTO implements Dto {
    private DepartmentDTO departmentDto;
    private List<LabelDTO> labelDtos;

    public DepartmentDetailsDTO() {}

    public DepartmentDetailsDTO(DepartmentDTO departmentDto, List<LabelDTO> labelDtos) {
        this.departmentDto = departmentDto;
        this.labelDtos = labelDtos;
    }

    public DepartmentDTO getDepartmentDto() {return departmentDto;}

    public void setDepartmentDto(DepartmentDTO departmentDto) {this.departmentDto = departmentDto;}

    public List<LabelDTO> getLabelDtos() {return labelDtos;}

    public void setLabelDtos(List<LabelDTO> labelDtos) {this.labelDtos = labelDtos;}
}
