package uk.ac.stfc.facilities.domains.establishment;

import jakarta.persistence.NoResultException;

import java.util.List;

public interface EstablishmentService {

    List<Establishment> getAllEstablishments();

    List<Establishment> getEstablishmentsByQuery(String searchQuery, boolean useAliases, boolean onlyVerified, int limit);

    List<RorSchemaV21> getRorMatches(String establishmentName);

    Establishment addRorDataToEstablishment(Long establishmentId, RorSchemaV21 ror);

    List<EstablishmentAlias> createEstablishmentAliasesFromRor(Long establishmentId, RorSchemaV21 ror);

    List<EstablishmentType> createEstablishmentTypesFromRor(Long establishmentId, RorSchemaV21 ror);

    Establishment createUnverifiedEstablishment(String name);

    Establishment deleteEstablishment(Long establishmentId) throws NoResultException;

    Establishment updateEstablishment(Long establishmentId, Establishment updateEst);

    List<Establishment> getUnverifiedEstablishments();

    List<EstablishmentAlias>  addEstablishmentAliases(List<EstablishmentAlias> aliases);

    List<EstablishmentType> addEstablishmentTypes(List<EstablishmentType> types);
}
