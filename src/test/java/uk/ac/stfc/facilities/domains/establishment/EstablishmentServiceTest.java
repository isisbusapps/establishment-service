package uk.ac.stfc.facilities.domains.establishment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static uk.ac.stfc.facilities.domains.establishment.EstablishmentTestConstants.ESTABLISHMENT_SEARCH_LIMIT;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EstablishmentServiceTest {

    @Mock
    private EstablishmentRepository repo;

    @Mock
    private EstablishmentAliasRepository aliasRepo;

    @Mock
    private EstablishmentCategoryRepository typeRepo;

    @InjectMocks
    EstablishmentServiceImpl service;

    @Test
    void test_getEstablishmentsByQuery_ExactQuery_ReturnsEstablishments() {
        Establishment oxford = new Establishment(1L, "University of Oxford");
        when(repo.getAll()).thenReturn(List.of(oxford));

        List<Establishment> result = service.getEstablishmentsByQuery("University of Oxford", false, false, ESTABLISHMENT_SEARCH_LIMIT);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("University of Oxford", result.get(0).getEstablishmentName());
    }

    @Test
    void test_getEstablishmentsByQuery_QueryAlias_ReturnsEstablishments() {
        Establishment kingsCollegeLondon = new Establishment(2L, "King’s College London");
        when(repo.getAll()).thenReturn(List.of(kingsCollegeLondon));
        when(aliasRepo.getAliasesFromEstablishment(2L))
                .thenReturn(List.of(new EstablishmentAlias(1l, 2L, "kcl")));

        List<Establishment> resultWithoutAlias = service.getEstablishmentsByQuery("kcl", false, false, ESTABLISHMENT_SEARCH_LIMIT);
        List<Establishment> resultWithAlias = service.getEstablishmentsByQuery("kcl", true, false, ESTABLISHMENT_SEARCH_LIMIT);

        Assertions.assertEquals(0, resultWithoutAlias.size());
        Assertions.assertEquals(1, resultWithAlias.size());
        Assertions.assertEquals("King’s College London", resultWithAlias.get(0).getEstablishmentName());
    }

    @Test
    void test_getEstablishmentsByQuery_NoMatchQuery_ReturnsEmptyList() {
        Establishment universityCollegeLondon = new Establishment(3L, "University College London");
        when(repo.getAll()).thenReturn(List.of(universityCollegeLondon));

        List<Establishment> result = service.getEstablishmentsByQuery("Harvard", false, false, ESTABLISHMENT_SEARCH_LIMIT);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void test_getEstablishmentsByQuery_EmptyInput_ReturnsEmptyList() {
        Establishment universityCollegeLondon = new Establishment(3L, "University College London");
        when(repo.getAll()).thenReturn(List.of(universityCollegeLondon));

        List<Establishment> result = service.getEstablishmentsByQuery("", false, false, ESTABLISHMENT_SEARCH_LIMIT);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void test_getEstablishmentsByQuery_CaseInsensitiveQuery_ReturnsEstablishments() {
        Establishment oxford = new Establishment(1L, "University of Oxford");
        when(repo.getAll()).thenReturn(List.of(oxford));

        List<Establishment> result = service.getEstablishmentsByQuery("UNIVERSITY OF OXFORD", false, false, ESTABLISHMENT_SEARCH_LIMIT);

        Assertions.assertEquals("University of Oxford", result.get(0).getEstablishmentName());
    }

    @Test
    void test_getEstablishmentsByQuery_PartialNameQuery_ReturnsEstablishments() {
        Establishment oxford = new Establishment(1L, "University of Oxford");
        when(repo.getAll()).thenReturn(List.of(oxford));

        List<Establishment> results = service.getEstablishmentsByQuery("ox", false,false, ESTABLISHMENT_SEARCH_LIMIT);

        Assertions.assertEquals("University of Oxford", results.get(0).getEstablishmentName());
    }

    @Test
    void test_getEstablishmentsByQuery_TypoInQuery_ReturnsEstablishments() {
        Establishment oxford = new Establishment(1L, "University of Oxford");
        when(repo.getAll()).thenReturn(List.of(oxford));

        List<Establishment> results = service.getEstablishmentsByQuery("oxfd", false, false, ESTABLISHMENT_SEARCH_LIMIT);

        Assertions.assertEquals("University of Oxford", results.get(0).getEstablishmentName());
    }

    @Test
    void test_getEstablishmentsByQuery_OnlyVerifiedEstablishments_ReturnsEstablishments() {
        Establishment oxfordUnverified = new Establishment(5L, "Oxford University");
        Establishment oxfordVerified = new Establishment(1L, "University of Oxford");
        when(repo.getAll()).thenReturn(List.of(oxfordUnverified, oxfordVerified));
        when(repo.getVerified()).thenReturn(List.of(oxfordVerified));

        List<Establishment> results = service.getEstablishmentsByQuery("oxford university", false, true, ESTABLISHMENT_SEARCH_LIMIT);

        boolean containsOxfordUnverified = results.stream().anyMatch(result -> "Oxford University".equals(result.getEstablishmentName()));
        boolean containsOxfordVerified = results.stream().anyMatch(result -> "University of Oxford".equals(result.getEstablishmentName()));
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
        when(repo.getVerified()).thenReturn(allEstablishments);
        int limit = ESTABLISHMENT_SEARCH_LIMIT;

        List<Establishment> allResults = service.getEstablishmentsByQuery("King's College London", true, true, ESTABLISHMENT_SEARCH_LIMIT);

        List<Establishment> topResults = service.getEstablishmentsByQuery("King's College London", true, true, 3);

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
        when(repo.findById(est.getEstablishmentId())).thenReturn(est);

        Establishment result =  service.addRorDataToEstablishment(est.getEstablishmentId(), ror);

        Assertions.assertEquals("University of Amsterdam", result.getEstablishmentName(), "entity has wrong name");
        Assertions.assertEquals("https://ror.org/04dkp9463", result.getRorId(), "entity has wrong ROR id");
        Assertions.assertEquals("The Netherlands", result.getCountryName(), "entity has wrong country name");
        Assertions.assertEquals("https://www.uva.nl", result.getEstablishmentUrl(), "entity has wrong url");
    }

    @Test
    void test_addEstablishmentAliasesFromRor_RorData_ReturnsEstablishmentAliases() {
        RorSchemaV21 ror = service.getRorMatches("University of Amsterdam").getFirst();
        Establishment est = new  Establishment(4L, "Amsterdam");
        when(repo.findById(est.getEstablishmentId())).thenReturn(est);

        List<EstablishmentAlias> results =  service.addEstablishmentAliasesFromRor(est.getEstablishmentId(), ror);

        List<Long> expectedEstId = List.of(est.getEstablishmentId(),est.getEstablishmentId());
        List<String> expectedAliases = List.of("Universiteit van Amsterdam", "UvA");
        Assertions.assertEquals(expectedEstId, results.stream().map(EstablishmentAlias::getEstablishmentId).toList(), "unexpected establishment id");
        Assertions.assertEquals(expectedAliases, results.stream().map(EstablishmentAlias::getAlias).toList(), "unexpected aliases");
    }

    @Test
    void test_addEstablishmentCategoriesFromRor_RorData_ReturnsEstablishmentCategoryLinks() {
        RorSchemaV21 ror = service.getRorMatches("University of Amsterdam").getFirst();
        Establishment est = new  Establishment(4L, "Amsterdam");
        when(repo.findById(est.getEstablishmentId())).thenReturn(est);

        List<EstablishmentCategoryLink> results =  service.addEstablishmentCategoriesFromRor(est.getEstablishmentId(), ror);

        List<Long> expectedEstId = List.of(est.getEstablishmentId(),est.getEstablishmentId());
        List<String> expectedCategories = List.of("education", "funder");
        Assertions.assertEquals(expectedEstId, results.stream().map(EstablishmentCategoryLink::getEstablishmentId).toList(), "unexpected establishment id");
        Assertions.assertEquals(expectedCategories, results.stream().map(EstablishmentCategoryLink::getType).toList(), "unexpected categories");
    }

    @Test
    void test_createUnverifiedEstablishment_InputEstablishmentName_ReturnsEstablishment() {
        String name = "Test University";

        Establishment result = service.createUnverifiedEstablishment(name);

        Assertions.assertEquals(name, result.getEstablishmentName());
        Assertions.assertFalse(result.getVerified());
    }

    @Test
    void test_updateEstablishment_EstablishmentUpdate_ReturnsUpdatedEstablishment() {

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

        Establishment updated = new Establishment(
                4L,
                "New Name",
                "new-ror",
                "New Country",
                "http://new.url",
                null,
                null,
                true
        );

        when(repo.findById(existing.getEstablishmentId())).thenReturn(existing);

        Establishment result = service.updateEstablishment(existing.getEstablishmentId(), updated);

        Assertions.assertEquals("New Name", result.getEstablishmentName(), "Establishment name should be updated");
        Assertions.assertEquals("new-ror", result.getRorId(), "ROR ID should be updated");
        Assertions.assertEquals("New Country", result.getCountryName(), "Country name should be updated");
        Assertions.assertEquals("http://new.url", result.getEstablishmentUrl(), "URL should be updated");
        Assertions.assertEquals(true, result.getVerified(), "Verified flag should be updated");

        Assertions.assertEquals(1L, result.getEstablishmentId(), "Establishment ID should remain unchanged");

        Assertions.assertSame(existing, result, "Should return the same object instance after modification");
    }

    // Tests with a sample of real data to evaluate fuzzy matching results for development. Not part of unit tests.

    @Disabled("test_getEstablishmentsByQuery_PerformanceTest_CompletesWithinExpectedTime() is @Disabled")
    @Test
    void test_getEstablishmentsByQuery_PerformanceTest_CompletesWithinExpectedTime() {
        EstablishmentService service = new EstablishmentServiceImpl(
                new EstablishmentRepositoryCSV(),
                new EstablishmentAliasRepositoryCSV(),
                typeRepo
        );

        long start = System.currentTimeMillis();
        List<Establishment> results = service.getEstablishmentsByQuery("kcl", true, false, ESTABLISHMENT_SEARCH_LIMIT);
        long end = System.currentTimeMillis();

        System.out.println("Query took: " + (end - start) + "ms");

        Assertions.assertFalse(results.isEmpty());
        Assertions.assertTrue((end - start) < 200);
    }

    @Disabled("test_getEstablishmentsByQuery_SampleRealDataQuery_PrintsResults() is @Disabled")
    @Test
    void test_getEstablishmentsByQuery_SampleRealDataQuery_PrintsResults() {
        EstablishmentService service = new EstablishmentServiceImpl(
                new EstablishmentRepositoryCSV(),
                new EstablishmentAliasRepositoryCSV(),
                typeRepo
        );

        List<Establishment> results = service.getEstablishmentsByQuery("ucl", true, true,ESTABLISHMENT_SEARCH_LIMIT);

        System.out.println("Establishments returned in order:");
        results.forEach(est -> System.out.println(est.getEstablishmentName()));
    }
}
