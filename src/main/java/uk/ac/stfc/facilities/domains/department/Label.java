package uk.ac.stfc.facilities.domains.department;

public class Label {
    private Long labelId;
    private String labelName;

    public Label(Long labelId, String labelName) {
        this.labelId = labelId;
        this.labelName = labelName;
    }

    public Long getLabelId() {return labelId;}

    public void setLabelId(Long labelId) {this.labelId = labelId;}

    public String getName() {return labelName;}

    public void setName(String labelName) {this.labelName = labelName;}
}
