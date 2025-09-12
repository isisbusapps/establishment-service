package uk.ac.rl.facilities.establishment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import uk.ac.rl.facilities.impl.domains.establishment.*;
import uk.ac.rl.facilities.impl.mappers.CategoryMapper;
import uk.ac.rl.facilities.impl.mappers.EstablishmentAliasMapper;
import uk.ac.rl.facilities.impl.mappers.EstablishmentMapper;
import uk.rl.ac.facilities.api.domains.establishment.EstablishmentModel;
import uk.rl.ac.facilities.api.domains.establishment.EstablishmentService;
import uk.rl.ac.facilities.facilities.api.generated.ror.RorSchemaV21;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;

import static org.mockito.Mockito.*;
import static uk.ac.rl.facilities.establishment.EstablishmentTestConstants.ESTABLISHMENT_SEARCH_LIMIT;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EstablishmentServiceTest {

    @Mock
    private EstablishmentRepository estRepo;

    @Mock
    private EstablishmentAliasRepository aliasRepo;

    @Mock
    private CategoryRepository categoryRepo;

    @Mock
    private EstablishmentCategoryLinkRepository estCatRepo;

    private EstablishmentMapper estMapper = Mappers.getMapper(EstablishmentMapper.class);
    private CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);
    private EstablishmentAliasMapper aliasMapper = Mappers.getMapper(EstablishmentAliasMapper.class);

    EstablishmentService service;

    @BeforeEach
    void setUp() {
        service = new EstablishmentServiceImpl(estRepo, categoryRepo, estCatRepo, aliasRepo, estMapper, aliasMapper, categoryMapper);
    }

    @Test
    void test_getEstablishmentsByQuery_ExactQuery_ReturnsEstablishments() {
        Establishment oxford = new Establishment(1L, "University of Oxford");
        when(estRepo.getAll()).thenReturn(List.of(oxford));

        List<EstablishmentModel> result = service.getEstablishmentsByQuery("University of Oxford", false, false, ESTABLISHMENT_SEARCH_LIMIT);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("University of Oxford", result.getFirst().getName());
    }

    @Test
    void test_getEstablishmentsByQuery_QueryAlias_ReturnsEstablishments() {
        Establishment kingsCollegeLondon = new Establishment(2L, "King’s College London");
        when(estRepo.getAll()).thenReturn(List.of(kingsCollegeLondon));
        when(aliasRepo.getAliasesFromEstablishment(2L))
                .thenReturn(List.of(new EstablishmentAlias(1L, 2L, "kcl")));

        List<EstablishmentModel> resultWithoutAlias = service.getEstablishmentsByQuery("kcl", false, false, ESTABLISHMENT_SEARCH_LIMIT);
        List<EstablishmentModel> resultWithAlias = service.getEstablishmentsByQuery("kcl", true, false, ESTABLISHMENT_SEARCH_LIMIT);

        Assertions.assertEquals(0, resultWithoutAlias.size());
        Assertions.assertEquals(1, resultWithAlias.size());
        Assertions.assertEquals("King’s College London", resultWithAlias.getFirst().getName());
    }

    @Test
    void test_getEstablishmentsByQuery_NoMatchQuery_ReturnsEmptyList() {
        Establishment universityCollegeLondon = new Establishment(3L, "University College London");
        when(estRepo.getAll()).thenReturn(List.of(universityCollegeLondon));

        List<EstablishmentModel> result = service.getEstablishmentsByQuery("Harvard", false, false, ESTABLISHMENT_SEARCH_LIMIT);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void test_getEstablishmentsByQuery_EmptyInput_ReturnsEmptyList() {
        Establishment universityCollegeLondon = new Establishment(3L, "University College London");
        when(estRepo.getAll()).thenReturn(List.of(universityCollegeLondon));

        List<EstablishmentModel> result = service.getEstablishmentsByQuery("", false, false, ESTABLISHMENT_SEARCH_LIMIT);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void test_getEstablishmentsByQuery_CaseInsensitiveQuery_ReturnsEstablishments() {
        Establishment oxford = new Establishment(1L, "University of Oxford");
        when(estRepo.getAll()).thenReturn(List.of(oxford));

        List<EstablishmentModel> result = service.getEstablishmentsByQuery("UNIVERSITY OF OXFORD", false, false, ESTABLISHMENT_SEARCH_LIMIT);

        Assertions.assertEquals("University of Oxford", result.getFirst().getName());
    }

    @Test
    void test_getEstablishmentsByQuery_PartialNameQuery_ReturnsEstablishments() {
        Establishment oxford = new Establishment(1L, "University of Oxford");
        when(estRepo.getAll()).thenReturn(List.of(oxford));

        List<EstablishmentModel> results = service.getEstablishmentsByQuery("ox", false,false, ESTABLISHMENT_SEARCH_LIMIT);

        Assertions.assertEquals("University of Oxford", results.getFirst().getName());
    }

    @Test
    void test_getEstablishmentsByQuery_TypoInQuery_ReturnsEstablishments() {
        Establishment oxford = new Establishment(1L, "University of Oxford");
        when(estRepo.getAll()).thenReturn(List.of(oxford));

        List<EstablishmentModel> results = service.getEstablishmentsByQuery("oxfd", false, false, ESTABLISHMENT_SEARCH_LIMIT);

        Assertions.assertEquals("University of Oxford", results.getFirst().getName());
    }

    @Test
    void test_getEstablishmentsByQuery_OnlyVerifiedEstablishments_ReturnsEstablishments() {
        Establishment oxfordUnverified = new Establishment(5L, "Oxford University");
        Establishment oxfordVerified = new Establishment(1L, "University of Oxford");
        when(estRepo.getAll()).thenReturn(List.of(oxfordUnverified, oxfordVerified));
        when(estRepo.getVerified()).thenReturn(List.of(oxfordVerified));

        List<EstablishmentModel> results = service.getEstablishmentsByQuery("oxford university", false, true, ESTABLISHMENT_SEARCH_LIMIT);

        boolean containsOxfordUnverified = results.stream().anyMatch(result -> "Oxford University".equals(result.getName()));
        boolean containsOxfordVerified = results.stream().anyMatch(result -> "University of Oxford".equals(result.getName()));
        Assertions.assertFalse(containsOxfordUnverified, "Results should not contain Oxford University");
        Assertions.assertTrue(containsOxfordVerified, "Results should contain University of Oxford");
    }

    @Test
    void test_getEstablishmentsByQuery_ReturnsTop3Only() {
        Establishment est1 = new Establishment(1L, "King's College London");
        Establishment est2 = new Establishment(2L, "King's College Londo");
        Establishment est3 = new Establishment(3L, "King's College Lond");
        Establishment est4 = new Establishment(4L, "King' College Lon");

        List<Establishment> allEstablishments = List.of(est1, est2, est3, est4);
        when(estRepo.getVerified()).thenReturn(allEstablishments);

        List<EstablishmentModel> allResults = service.getEstablishmentsByQuery("King's College London", true, true, ESTABLISHMENT_SEARCH_LIMIT);

        List<EstablishmentModel> topResults = service.getEstablishmentsByQuery("King's College London", true, true, 3);

        Assertions.assertEquals(allEstablishments.size(), allResults.size(), "Should return all establishments");
        Assertions.assertEquals(3, topResults.size(),
                "Should return the top 3 establishments");

    }

    @Test
    void test_getRorMatches_Query_ReturnsRorMatches() {

        List<RorSchemaV21> results = service.getRorMatches("University of Oxford");

        Assertions.assertTrue(results.size()>1, "Multiple results expected to be returned");
        Assertions.assertEquals("https://ror.org/052gg0110", results.getFirst().getId(), "First result should be University of Oxford");
    }

    @Test
    void test_addRorDataToEstablishment_RorSchema_DataAddedToEntity() {
        RorSchemaV21 ror = service.getRorMatches("University of Amsterdam").getFirst();
        Establishment est = new  Establishment(4L, "Amsterdam");
        when(estRepo.findById(est.getEstablishmentId())).thenReturn(est);

        EstablishmentModel result =  service.addRorDataToEstablishment(est.getEstablishmentId(), ror);

        Assertions.assertEquals("University of Amsterdam", result.getName(), "entity has wrong name");
        Assertions.assertEquals("https://ror.org/04dkp9463", result.getRorID(), "entity has wrong ROR id");
        Assertions.assertEquals("The Netherlands", result.getCountry(), "entity has wrong country name");
        Assertions.assertEquals("https://www.uva.nl", result.getUrl().toString(), "entity has wrong url");
    }

    @Test
    void test_addEstablishmentAliasesFromRor_RorData_ReturnsEstablishmentAliases() {
        RorSchemaV21 ror = service.getRorMatches("University of Amsterdam").getFirst();
        Establishment est = new  Establishment(4L, "Amsterdam");
        when(estRepo.findById(est.getEstablishmentId())).thenReturn(est);

        List<String> results =  service.addEstablishmentAliasesFromRor(est.getEstablishmentId(), ror);

        List<Long> expectedEstId = List.of(est.getEstablishmentId(),est.getEstablishmentId());
        List<String> expectedAliases = List.of("Universiteit van Amsterdam", "UvA");
        Assertions.assertEquals(expectedAliases, results.stream().toList(), "unexpected aliases");
    }

    @Test
    void test_createUnverifiedEstablishment_InputEstablishmentName_ReturnsEstablishment() {
        String name = "Test University";

        EstablishmentModel result = service.createUnverifiedEstablishment(name);

        Assertions.assertEquals(name, result.getName(), "unexpected name");
        Assertions.assertFalse(result.getVerified());
    }

    @Test
    void test_updateEstablishment_EstablishmentUpdate_ReturnsUpdatedEstablishment() throws MalformedURLException {

        Establishment existing = new Establishment(
                1L,
                "Old Name",
                "old-ror",
                "Old Country",
                "http://old.url",
                null,
                null,
                false
        );

        EstablishmentModel updated = new EstablishmentModel(
                4L,
                "new-ror",
                "New Name",
                "New Country",
                URI.create("http://new.url").toURL(),
                null,
                null,
                true
        );

        when(estRepo.findById(existing.getEstablishmentId())).thenReturn(existing);

        EstablishmentModel result = service.updateEstablishment(existing.getEstablishmentId(), updated);

        Assertions.assertEquals("New Name", result.getName(), "Establishment name should be updated");
        Assertions.assertEquals("new-ror", result.getRorID(), "ROR ID should be updated");
        Assertions.assertEquals("New Country", result.getCountry(), "Country name should be updated");
        Assertions.assertEquals("http://new.url", result.getUrl().toString(), "URL should be updated");
        Assertions.assertEquals(true, result.getVerified(), "Verified flag should be updated");

        Assertions.assertEquals(existing.getEstablishmentId(), result.getId(), "Establishment ID should remain unchanged");

    }
}
