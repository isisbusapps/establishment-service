package uk.ac.stfc.facilities.domains.establishment;

import com.stfc.UserOffice.Exceptions.DuplicateRecordsException;
import com.stfc.UserOffice.Exceptions.EstablishmentDTOException;
import jakarta.persistence.NoResultException;

import java.util.List;

public interface EstablishmentService {

    List<EstablishmentDTO> getAllEstablishmentDTOs();

    List<Establishment> getEstablishmentsByQuery(String searchQuery, boolean useAliases, boolean onlyVerified);

    List<Establishment> getTopEstablishmentsByQuery(String searchQuery, boolean useAliases, boolean onlyVerified, int limit);

    EstablishmentDTO getEstablishmentWithAddress(Long establishmentId) throws EstablishmentDTOException;

    List<EstablishmentDTO> getEstablishmentDTOsBySearchDetails(List<EstablishmentSearchDetails> searchDetails);

    RorQueryDto getRorMatches(String establishmentName);

    Establishment addRorDataToEstablishment(Establishment est, RorSchemaV21 ror);

    List<EstablishmentAlias> createEstablishmentAliasesFromRor(Establishment est, RorSchemaV21 ror);

    List<EstablishmentType> createEstablishmentTypesFromRor(Establishment est, RorSchemaV21 ror);

    EstablishmentDTO createEstablishmentFromEstablishmentDTO(EstablishmentDTO dto);

    Establishment createUnverifiedEstablishment(String name);

    EstablishmentDTO deleteEstablishment(Long estId) throws NoResultException;

    EstablishmentDTO updateEstablishmentWithAddress(EstablishmentDTO e) throws DuplicateRecordsException;

    Establishment updateEstablishment(Establishment existingEst, Establishment updateEst);

    EstablishmentDTO deleteEstablishmentById(Long establishmentId) throws DuplicateRecordsException;

    EstablishmentDTO mergeEstablishment(String oldEst) throws DuplicateRecordsException;

    EstablishmentDTO getEstablishmentById(Long establishmentId);

    List<Establishment> getUnverifiedEstablishments();

    List<EstablishmentDTO> getSimilarEstablishments(String establishmentId) throws EstablishmentDTOException;

    List<EstablishmentDTO> getEstablishmentsByOrgName(String orgName);

    List<EstablishmentAlias>  addEstablishmentAliases(List<EstablishmentAlias> aliases);

    List<EstablishmentType> addEstablishmentTypes(List<EstablishmentType> types);
}
