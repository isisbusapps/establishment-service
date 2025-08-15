package uk.ac.stfc.facilities.domains.department;

public class Department {
    private Long  departmentId;
    private String departmentName;
    private Long establishmentId;

    public Department() {}

    public Department(Long departmentId, String departmentName, Long establishmentId) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.establishmentId = establishmentId;
    }

    public Long getDepartmentId() {return departmentId;}

    public void setDepartmentId(Long departmentId) {this.departmentId = departmentId;}

    public String getDepartmentName() {return departmentName;}

    public void setDepartmentName(String departmentName) {this.departmentName = departmentName;}

    public Long getEstablishmentId() {return establishmentId;}

    public void setEstablishmentId(Long establishmentId) {this.establishmentId = establishmentId;}
}
