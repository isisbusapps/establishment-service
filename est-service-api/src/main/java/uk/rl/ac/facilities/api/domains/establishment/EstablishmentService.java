package uk.rl.ac.facilities.api.domains.establishment;

import uk.rl.ac.facilities.facilities.api.generated.ror.RorSchemaV21;

import java.util.List;

public interface EstablishmentService {

    List<EstablishmentModel> getAllEstablishments();

    EstablishmentModel getEstablishment(Long establishmentId);

    List<EstablishmentModel> getEstablishmentsByQuery(String searchQuery, boolean useAliases, boolean onlyVerified, int limit);

    List<RorSchemaV21> getRorMatches(String establishmentName);

    EstablishmentModel addRorDataToEstablishment(Long establishmentId, RorSchemaV21 ror);

    List<AliasModel> addEstablishmentAliasesFromRor(Long establishmentId, RorSchemaV21 ror);

    List<CategoryModel> addEstablishmentCategoryLinksFromRor(Long establishmentId, RorSchemaV21 ror);

    EstablishmentModel createUnverifiedEstablishment(String name);

    void deleteEstablishment(Long establishmentId);

    EstablishmentModel updateEstablishment(Long establishmentId, EstablishmentModel updateEst);

    List<EstablishmentModel> getUnverifiedEstablishments();

    List<AliasModel> addEstablishmentAliases(Long establishmentId, List<String> aliasNames);

    List<CategoryModel> addEstablishmentCategoryLinks(Long establishmentId, List<Long> categoryIds);

    List<CategoryModel> getCategoriesForEstablishment(Long establishmentId);

    List<AliasModel> getAliasesForEstablishment(Long establishmentId);
}
