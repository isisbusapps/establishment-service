package uk.ac.rl.facilities.impl.domains.department;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DepartmentLabelLinkId implements Serializable {
    private Long departmentId;
    private Long labelId;

    public DepartmentLabelLinkId() {}

    public DepartmentLabelLinkId(Long departmentId, Long labelId) {
        this.departmentId = departmentId;
        this.labelId = labelId;
    }

    public Long getDepartmentId() {return departmentId;}

    public void setDepartmentId(Long departmentId) {this.departmentId = departmentId;}

    public Long getLabelId() {return labelId;}

    public void setLabelId(Long labelId) {this.labelId = labelId;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DepartmentLabelLinkId)) return false;
        DepartmentLabelLinkId that = (DepartmentLabelLinkId) o;
        return Objects.equals(departmentId, that.departmentId) &&
                Objects.equals(labelId, that.labelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, labelId);
    }
}
