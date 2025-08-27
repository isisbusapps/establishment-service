package uk.ac.stfc.facilities.domains.department;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DepartmentLabelId implements Serializable {
    private Long departmentId;
    private Long labelId;

    public DepartmentLabelId() {}

    public DepartmentLabelId(Long departmentId, Long labelId) {
        this.departmentId = departmentId;
        this.labelId = labelId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getLabelId() {
        return labelId;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DepartmentLabelId)) return false;
        DepartmentLabelId that = (DepartmentLabelId) o;
        return Objects.equals(departmentId, that.departmentId) &&
                Objects.equals(labelId, that.labelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, labelId);
    }
}
