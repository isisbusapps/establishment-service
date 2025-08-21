package uk.ac.stfc.facilities.helpers;

import uk.ac.stfc.facilities.domains.establishment.EstablishmentAlias;
import uk.ac.stfc.facilities.domains.establishment.EstablishmentDTO;
import uk.ac.stfc.facilities.domains.establishment.EstablishmentType;

import java.util.List;

public class EnrichedEstablishmentResponse {
    private EstablishmentDTO establishment;
    private List<EstablishmentAlias> aliases;
    private List<EstablishmentType> types;

    public EnrichedEstablishmentResponse() {}

    public EnrichedEstablishmentResponse(EstablishmentDTO establishment, List<EstablishmentAlias> aliases, List<EstablishmentType> types) {
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

    public List<EstablishmentType> getTypes() {
        return types;
    }

    public void setTypes(List<EstablishmentType> types) {
        this.types = types;
    }
}
