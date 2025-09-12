package uk.rl.ac.facilities.api.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class LabelDTO {

    @Schema(
            description = "The unique identifier for the label",
            example = "1",
            required = true,
            readOnly = true
    )
    private Long labelId;

    @Schema(
            description = "The name of the label",
            example = "Physics",
            required = true,
            nullable = false
    )
    private String labelName;

    public Long getLabelId() {return labelId;}

    public void setLabelId(Long labelId) {this.labelId = labelId;}

    public String getLabelName() {return labelName;}

    public void setLabelName(String labelName) {this.labelName = labelName;}
}

