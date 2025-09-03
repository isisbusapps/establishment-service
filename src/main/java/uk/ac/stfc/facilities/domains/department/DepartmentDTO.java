package uk.ac.stfc.facilities.domains.department;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import uk.ac.stfc.facilities.BaseClasses.Dto;

public class DepartmentDTO implements Dto {

    @Schema(
            description = "The unique identifier for the department",
            example = "1",
            required = true,
            readOnly = true
    )
    private Long departmentId;

    @Schema(
            description = "The name of the department",
            example = "Physics Department",
            required = true,
            nullable = true
    )
    private String departmentName;

    @Schema(
            description = "The old establishment ID associated with the department, if any",
            example = "10",
            nullable = true
    )
    private Long oldEstablishmentId;

    @Schema(
            description = "The current establishment ID associated with the department",
            example = "1",
            nullable = true
    )
    private Long establishmentId;

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {this.departmentId = departmentId;}

    public String getDepartmentName() {return departmentName;}

    public void setDepartmentName(String departmentName) {this.departmentName = departmentName;}

    public Long getOldEstablishmentId() {return oldEstablishmentId;}

    public void setOldEstablishmentId(Long oldEstablishmentId) {this.oldEstablishmentId = oldEstablishmentId;}

    public Long getEstablishmentId() {return establishmentId;}

    public void setEstablishmentId(Long establishmentId) {this.establishmentId = establishmentId;}
}
