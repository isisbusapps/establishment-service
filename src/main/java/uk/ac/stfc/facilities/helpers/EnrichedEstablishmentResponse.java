package uk.ac.stfc.facilities.helpers;

import uk.ac.stfc.facilities.domains.establishment.EstablishmentAlias;
import uk.ac.stfc.facilities.domains.establishment.EstablishmentDTO;
import uk.ac.stfc.facilities.domains.establishment.EstablishmentCategoryLink;

import java.util.List;

public class EnrichedEstablishmentResponse {
    private EstablishmentDTO establishment;
    private List<EstablishmentAlias> aliases;
    private List<EstablishmentCategoryLink> types;

    public EnrichedEstablishmentResponse() {}

    public EnrichedEstablishmentResponse(EstablishmentDTO establishment, List<EstablishmentAlias> aliases, List<EstablishmentCategoryLink> types) {
        this.establishment = establishment;
        this.aliases = aliases;
        this.types = types;
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

    public List<EstablishmentCategoryLink> getTypes() {
        return types;
    }

    public void setTypes(List<EstablishmentCategoryLink> types) {
        this.types = types;
    }
}
