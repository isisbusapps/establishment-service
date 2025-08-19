package uk.ac.stfc.facilities.domains.establishment;

import jakarta.persistence.NoResultException;

import java.util.List;

public interface EstablishmentService {

    List<Establishment> getAllEstablishments();

    List<Establishment> getEstablishmentsByQuery(String searchQuery, boolean useAliases, boolean onlyVerified);

    List<Establishment> getTopEstablishmentsByQuery(String searchQuery, boolean useAliases, boolean onlyVerified, int limit);

    RorQueryDto getRorMatches(String establishmentName);

    Establishment addRorDataToEstablishment(Establishment est, RorSchemaV21 ror);

    List<EstablishmentAlias> createEstablishmentAliasesFromRor(Establishment est, RorSchemaV21 ror);

    List<EstablishmentType> createEstablishmentTypesFromRor(Establishment est, RorSchemaV21 ror);

    Establishment createUnverifiedEstablishment(String name);

    Establishment deleteEstablishment(Long establishmentId) throws NoResultException;

    Establishment updateEstablishment(Establishment existingEst, Establishment updateEst);

    Establishment getEstablishmentById(Long establishmentId);

    List<Establishment> getUnverifiedEstablishments();

    List<EstablishmentAlias>  addEstablishmentAliases(List<EstablishmentAlias> aliases);

    List<EstablishmentType> addEstablishmentTypes(List<EstablishmentType> types);
}
