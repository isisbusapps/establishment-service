package uk.ac.stfc.facilities.domains.establishment;

import java.util.List;

public class EstablishmentDetailsDTO {
    private EstablishmentDTO establishment;
    private List<EstablishmentAlias> aliases;
    private List<EstablishmentCategoryLink> categories;

    public EstablishmentDetailsDTO() {}

    public EstablishmentDetailsDTO(EstablishmentDTO establishment, List<EstablishmentAlias> aliases, List<EstablishmentCategoryLink> categories) {
        this.establishment = establishment;
        this.aliases = aliases;
        this.categories = categories;
    }

    public EstablishmentDTO getEstablishment() {
        return establishment;
    }

    public void setEstablishment(EstablishmentDTO establishment) {
        this.establishment = establishment;
    }

    public List<EstablishmentAlias> getAliases() {
        return aliases;
    }

    public void setAliases(List<EstablishmentAlias> aliases) {
        this.aliases = aliases;
    }

    public List<EstablishmentCategoryLink> getCategories() {
        return categories;
    }

    public void setCategories(List<EstablishmentCategoryLink> categories) {
        this.categories = categories;
    }
}
