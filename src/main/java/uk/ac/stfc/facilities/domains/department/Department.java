package uk.ac.stfc.facilities.domains.department;

import jakarta.persistence.*;

@Entity
@Table(name = "DEPARTMENT")
public class Department {

    @Id
    @SequenceGenerator(name="DEPARTMENT_RID_SEQ", sequenceName="DEPARTMENT_RID_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEPARTMENT_RID_SEQ")
    @Column(name = "ID")
    private Long  departmentId;

    @Column(name = "DEPARTMENT_NAME")
    private String departmentName;

    @Column(name = "OLD_ESTABLISHMENT_ID")
    private Long oldEstablishmentId;

    private Long establishmentId;

    public Department() {}

    public Department(Long departmentId, String departmentName, Long establishmentId) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.establishmentId = establishmentId;
    }

    public Department(Long departmentId, String departmentName, Long oldEstablishmentId, Long establishmentId) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.oldEstablishmentId = oldEstablishmentId;
        this.establishmentId = establishmentId;
    }


    public Long getDepartmentId() {return departmentId;}

    public void setDepartmentId(Long departmentId) {this.departmentId = departmentId;}

    public String getDepartmentName() {return departmentName;}

    public void setDepartmentName(String departmentName) {this.departmentName = departmentName;}

    public Long getOldEstablishmentId() {return oldEstablishmentId;}

    public void setOldEstablishmentId(Long oldEstablishmentId) {this.oldEstablishmentId = oldEstablishmentId;}

    public Long getEstablishmentId() {return establishmentId;}

    public void setEstablishmentId(Long establishmentId) {this.establishmentId = establishmentId;}
}
