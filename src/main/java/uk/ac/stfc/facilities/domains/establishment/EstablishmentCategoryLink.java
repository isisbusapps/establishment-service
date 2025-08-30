package uk.ac.stfc.facilities.domains.establishment;

import jakarta.persistence.*;

@Entity
@Table(name = "ESTABLISHMENT_CATEGORY_LINK")
public class EstablishmentCategoryLink {

    @EmbeddedId
    private EstablishmentCategoryLinkId id;

    @ManyToOne
    @MapsId("establishmentId")
    @JoinColumn(name = "ESTABLISHMENT_ID")
    private Establishment establishment;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    public EstablishmentCategoryLink() {}

    public EstablishmentCategoryLink(Establishment establishment, Category category) {
        this.establishment = establishment;
        this.category = category;
    }

    public EstablishmentCategoryLinkId getId() {return id;}

    public void setId(EstablishmentCategoryLinkId id) {this.id = id;}

    public Establishment getEstablishment() {return establishment;}

    public void setEstablishment(Establishment establishment) {this.establishment = establishment;}

    public Category getCategory() {return category;}

    public void setCategory(Category category) {this.category = category;}

    public Long getEstablishmentId() {return id != null ? id.getEstablishmentId() : null;}

    public Long getCategoryId() {return id != null ? id.getCategoryId() : null;}
}