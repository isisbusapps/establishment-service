package uk.ac.stfc.facilities.helpers;

public class CreateDepartmentRequest {
    private String name;
    private Long establishmentId;

    public CreateDepartmentRequest() {}

    public CreateDepartmentRequest(String name, Long establishmentId) {
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
