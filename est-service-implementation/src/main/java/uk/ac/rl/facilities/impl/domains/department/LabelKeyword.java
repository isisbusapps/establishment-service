package uk.ac.rl.facilities.impl.domains.department;

import jakarta.persistence.*;

@Entity
@Table(name = "LABEL_KEYWORD")
public class LabelKeyword {

    @Id
    @SequenceGenerator(name = "LABEL_KEYWORD_RID_SEQ", sequenceName = "LABEL_KEYWORD_RID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LABEL_KEYWORD_RID_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "KEYWORD", nullable = false)
    private String keyword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LABEL_ID", nullable = false)
    private Label label;

    public LabelKeyword() {
    }

    public LabelKeyword(Label label, String keyword) {
        this.label = label;
        this.keyword = keyword;
    }

    public LabelKeyword(Long id, Label label, String keyword) {
        this.id = id;
        this.label = label;
        this.keyword = keyword;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getKeyword() { return keyword; }

    public void setKeyword(String keyword) { this.keyword = keyword; }

    public Label getLabel() { return label; }

    public void setLabel(Label label) { this.label = label; }
}
