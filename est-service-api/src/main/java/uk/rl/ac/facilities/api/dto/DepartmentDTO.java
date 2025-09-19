package uk.rl.ac.facilities.api.dto;


import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

public class DepartmentDTO {

    @Schema(
            description = "The unique identifier for the department",
            example = "1",
            required = true,
            readOnly = true
    )
    private Long id;

    @Schema(
            description = "The name of the department",
            example = "Physics Department",
            required = true,
            nullable = true
    )
    private String name;

    @Schema(
            description = "The old establishment ID associated with the department, if any",
            example = "10",
            nullable = true
    )
    private Long oldEstId;

    @Schema(
            description = "The current establishment ID associated with the department",
            example = "1",
            nullable = true
    )
    private Long estId;

    @Schema(
            description = "",
            example = "",
            nullable = true
    )
    private List<String> labels;

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

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
}
