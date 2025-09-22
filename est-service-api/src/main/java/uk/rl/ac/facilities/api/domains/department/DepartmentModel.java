package uk.rl.ac.facilities.api.domains.department;

import java.util.List;

public class DepartmentModel {

    private Long id;

    private String name;

    private Long oldEstId;

    private Long estId;

    private List<LabelModel> labels;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOldEstId() {
        return oldEstId;
    }

    public void setOldEstId(Long oldEstId) {
        this.oldEstId = oldEstId;
    }

    public Long getEstId() {
        return estId;
    }

    public void setEstId(Long estId) {
        this.estId = estId;
    }

    public List<LabelModel> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelModel> labels) {
        this.labels = labels;
    }
}
