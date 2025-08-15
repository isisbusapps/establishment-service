package uk.ac.stfc.facilities.domains.department;

public class DepartmentLabel {
    private Long rid;
    private Long departmentId;
    private Long labelId;

    public DepartmentLabel() {
    }

    public DepartmentLabel(Long departmentId, Long labelId) {
        this.departmentId = departmentId;
        this.labelId = labelId;
    }

    public DepartmentLabel(Long rid, Long departmentId, Long labelId) {
        this.rid = rid;
        this.departmentId = departmentId;
        this.labelId = labelId;
    }

    public Long getRid() { return rid; }

    public void setRid(Long rid) { this.rid = rid; }

    public Long getDepartmentId() { return departmentId; }

    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public Long getLabelId() { return labelId; }

    public void setLabelId(Long labelId) { this.labelId = labelId; }
}
