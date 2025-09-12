package uk.ac.rl.facilities.impl.domains.department;

import jakarta.persistence.*;

@Entity
@Table(name = "LABEL")
public class Label {

    @Id
    @SequenceGenerator(name="LABEL_RID_SEQ", sequenceName="LABEL_RID_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LABEL_RID_SEQ")
    @Column(name = "ID")
    private Long labelId;

    @Column(name = "LABEL_NAME")
    private String labelName;

    public Label() {}

    public Label(Long labelId, String labelName) {
        this.labelId = labelId;
        this.labelName = labelName;
    }

    public Long getLabelId() {return labelId;}

    public void setLabelId(Long labelId) {this.labelId = labelId;}

    public String getLabelName() {return labelName;}

    public void setLabelName(String labelName) {this.labelName = labelName;}
}
