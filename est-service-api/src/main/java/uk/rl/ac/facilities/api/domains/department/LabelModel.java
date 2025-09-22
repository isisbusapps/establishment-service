package uk.rl.ac.facilities.api.domains.department;

import java.util.List;

public class LabelModel {

    private Long id;

    private String labelName;

    private List<Long> departmentIDs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public List<Long> getDepartmentIDs() {
        return departmentIDs;
    }

    public void setDepartmentIDs(List<Long> departmentIDs) {
        this.departmentIDs = departmentIDs;
    }
}
