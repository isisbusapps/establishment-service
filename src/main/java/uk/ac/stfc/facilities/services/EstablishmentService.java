package uk.ac.stfc.facilities.services;

import jakarta.persistence.NoResultException;
import uk.ac.stfc.facilities.domains.establishment.Establishment;
import uk.ac.stfc.facilities.domains.establishment.EstablishmentAlias;
import uk.ac.stfc.facilities.domains.establishment.EstablishmentType;
import uk.ac.stfc.facilities.domains.establishment.RorQueryDto;

import java.util.List;

public interface EstablishmentService {

    List<Establishment> getAllEstablishmentDTOs();

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
