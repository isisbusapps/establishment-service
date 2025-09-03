package uk.ac.stfc.facilities.domains.establishment;

import java.util.List;

public class EstablishmentDetailsDTO {
    private Establishment establishment;
    private List<EstablishmentAlias> aliases;
    private List<Category> categories;

    public EstablishmentDetailsDTO() {}

    public EstablishmentDetailsDTO(Establishment establishment, List<EstablishmentAlias> aliases, List<Category> categories) {
        this.establishment = establishment;
        this.aliases = aliases;
        this.categories = categories;
    }

    public Establishment getEstablishment() {
        return establishment;
    }

    public void setEstablishment(Establishment establishment) {
        this.establishment = establishment;
    }

    public List<EstablishmentAlias> getAliases() {
        return aliases;
    }

    public void setAliases(List<EstablishmentAlias> aliases) {
        this.aliases = aliases;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
