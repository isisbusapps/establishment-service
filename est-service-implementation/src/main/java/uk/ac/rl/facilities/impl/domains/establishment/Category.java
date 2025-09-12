package uk.ac.rl.facilities.impl.domains.establishment;

import jakarta.persistence.*;

@Entity
@Table(name = "CATEGORY")
public class Category {

    @Id
    @SequenceGenerator(name="CATEGORY_RID_SEQ", sequenceName="CATEGORY_RID_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATEGORY_RID_SEQ")
    @Column(name = "ID")
    private Long categoryId;

    @Column(name = "CATEGORY_NAME")
    private String categoryName;

    public Category() {}

    public Category(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {return categoryId;}

    public void setCategoryId(Long categoryId) {this.categoryId = categoryId;}

    public String getCategoryName() {return categoryName;}

    public void setCategoryName(String categoryName) {this.categoryName = categoryName;}
}
