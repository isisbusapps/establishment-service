package uk.rl.ac.facilities.api.domains.department;

public class DepartmentLabelLinkIdModel {

    private Long departmentId;
    private Long labelId;

    public DepartmentLabelLinkIdModel(Long departmentId, Long labelId) {
        this.departmentId = departmentId;
        this.labelId = labelId;
    }
}
