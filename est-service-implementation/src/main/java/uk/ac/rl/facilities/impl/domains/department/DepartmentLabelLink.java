package uk.ac.rl.facilities.impl.domains.department;
import jakarta.persistence.*;

@Entity
@Table(name = "DEPARTMENT_LABEL_LINK")
public class DepartmentLabelLink {

    @EmbeddedId
    private DepartmentLabelLinkId id;

    @ManyToOne
    @MapsId("departmentId")
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department department;

    @ManyToOne
    @MapsId("labelId")
    @JoinColumn(name = "LABEL_ID")
    private Label label;

    public DepartmentLabelLink() { }

    public DepartmentLabelLink(Department department, Label label) {
        this.department = department;
        this.label = label;
        this.id = new DepartmentLabelLinkId(department.getDepartmentId(), label.getLabelId());
    }

    public DepartmentLabelLinkId getId() {return id;}

    public void setId(DepartmentLabelLinkId id) {this.id = id;}

    public Department getDepartment() {return department;}

    public void setDepartment(Department department) {this.department = department;}

    public Label getLabel() {return label;}

    public void setLabel(Label label) {this.label = label;}

    public Long getDepartmentId() {return id != null ? id.getDepartmentId() : null;}

    public Long getLabelId() {return id != null ? id.getLabelId() : null;}

}
