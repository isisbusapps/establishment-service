package uk.ac.stfc.facilities.domains.establishment;

import jakarta.persistence.NoResultException;
import org.hibernate.sql.Alias;

import java.util.List;

public interface EstablishmentService {

    List<Establishment> getAllEstablishments();

    Establishment getEstablishment(Long establishmentId);

    List<Establishment> getEstablishmentsByQuery(String searchQuery, boolean useAliases, boolean onlyVerified, int limit);

    List<RorSchemaV21> getRorMatches(String establishmentName);

    Establishment addRorDataToEstablishment(Long establishmentId, RorSchemaV21 ror);

    List<EstablishmentAlias> addEstablishmentAliasesFromRor(Long establishmentId, RorSchemaV21 ror);

    List<EstablishmentCategoryLink> addEstablishmentCategoryLinksFromRor(Long establishmentId, RorSchemaV21 ror);

    Establishment createUnverifiedEstablishment(String name);

    void deleteEstablishment(Long establishmentId) throws NoResultException;

    Establishment updateEstablishment(Long establishmentId, Establishment updateEst);

    List<Establishment> getUnverifiedEstablishments();

    List<EstablishmentAlias>  addEstablishmentAliases(Long establishmentId, List<String> aliasNames);

    List<EstablishmentCategoryLink> addEstablishmentCategoryLinks(Long establishmentId, List<Long> categoryIds);

    List<Category> getCategoriesForEstablishment(Long establishmentId);

    List<EstablishmentAlias> getAliasesForEstablishment(Long establishmentId);
}
