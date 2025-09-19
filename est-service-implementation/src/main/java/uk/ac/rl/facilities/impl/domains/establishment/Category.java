package uk.ac.rl.facilities.impl.domains.establishment;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "category", cascade =  {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<EstablishmentCategoryLink>  categoryLinks = new HashSet<>();

    public Category() {}

    public Category(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {return categoryId;}

    public void setCategoryId(Long categoryId) {this.categoryId = categoryId;}

    public String getCategoryName() {return categoryName;}

    public void setCategoryName(String categoryName) {this.categoryName = categoryName;}

    public Set<EstablishmentCategoryLink> getCategoryLinks() {
        return categoryLinks;
    }

    public void setCategoryLinks(Set<EstablishmentCategoryLink> categoryLinks) {
        this.categoryLinks = categoryLinks;
    }
}
