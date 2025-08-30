package uk.ac.stfc.facilities.domains.establishment;

import jakarta.persistence.Embeddable;
import uk.ac.stfc.facilities.domains.department.DepartmentLabelLinkId;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EstablishmentCategoryLinkId)) return false;
        EstablishmentCategoryLinkId that = (EstablishmentCategoryLinkId) o;
        return Objects.equals(establishmentId, that.establishmentId) &&
                Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(establishmentId, categoryId);
    }
}
