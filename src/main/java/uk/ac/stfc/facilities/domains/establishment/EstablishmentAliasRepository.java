package uk.ac.stfc.facilities.domains.establishment;

import java.util.List;

public interface EstablishmentAliasRepository {
    List<EstablishmentAlias> getAliasesFromEstablishment(Long establishmentId);
    void add(List<EstablishmentAlias> aliases);
}
