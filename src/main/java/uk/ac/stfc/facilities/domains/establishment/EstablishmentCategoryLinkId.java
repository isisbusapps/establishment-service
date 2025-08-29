package uk.ac.stfc.facilities.domains.establishment;

import java.io.Serializable;

public class EstablishmentCategoryLinkId implements Serializable {
    private Long establishmentId;
    private Long categoryId;

    public EstablishmentCategoryLinkId() {}

    public EstablishmentCategoryLinkId(Long establishmentId, Long categoryId) {
        this.establishmentId = establishmentId;
        this.categoryId = categoryId;
    }

    public Long getEstablishmentId() {return establishmentId;}

    public void setEstablishmentId(Long establishmentId) {this.establishmentId = establishmentId;}

    public Long getCategoryId() {return categoryId;}

    public void setCategoryId(Long categoryId) {this.categoryId = categoryId;}
}
