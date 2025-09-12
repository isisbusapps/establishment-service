package uk.rl.ac.facilities.api.dto;

public class CreateDepartmentDTO {

    private String name;
    private Long establishmentId;

    public CreateDepartmentDTO() {}

    public CreateDepartmentDTO(String name, Long establishmentId) {
        this.name = name;
        this.establishmentId = establishmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getEstablishmentId() {
        return establishmentId;
    }

    public void setEstablishmentId(Long establishmentId) {
        this.establishmentId = establishmentId;
    }
}
