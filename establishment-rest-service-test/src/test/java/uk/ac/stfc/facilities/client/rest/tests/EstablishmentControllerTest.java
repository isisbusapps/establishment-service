package uk.ac.stfc.facilities.client.rest.tests;

import io.restassured.filter.log.LogDetail;
import jakarta.json.Json;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import uk.ac.stfc.facilities.client.rest.base.RestTest;
import uk.ac.stfc.facilities.client.rest.resources.DepartmentData;
import uk.ac.stfc.facilities.client.rest.resources.EstablishmentData;
import uk.ac.stfc.facilities.client.rest.resources.CountryData;
import uk.ac.stfc.facilities.client.rest.resources.RorPayloadBuilder;

import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static uk.ac.stfc.facilities.client.rest.base.Constants.*;


public class EstablishmentControllerTest extends RestTest {
    @Override
    protected void injectData() throws Exception {
        injectData(EstablishmentData.data);
        injectData(DepartmentData.data);
        injectData(CountryData.data);
    }

    @Override
    protected void cleanupData() throws Exception {
        deleteTestData("LABEL_KEYWORD", "ID");
        deleteTestData("DEPARTMENT_LABEL_LINK", "DEPARTMENT_ID");
        deleteTestData("LABEL", "ID");
        deleteTestData("ESTABLISHMENT_ALIAS", "ALIAS_ID");
        deleteTestData("DEPARTMENT", "ID");

        deleteTestData("ESTABLISHMENT_ALIAS", "ESTABLISHMENT_ID");
        deleteTestData("ESTABLISHMENT_CATEGORY_LINK", "ESTABLISHMENT_ID");
        deleteTestData("CATEGORY", "ID");
        deleteTestData("ESTABLISHMENT_NEW", "ID");
        deleteTestData("ESTABLISHMENT_NEW", "ESTABLISHMENT_NAME", NEW_EST_NAME);
        deleteTestData("COUNTRY_NEW", "ID");
    }

    /* ----------------- getEstablishment ----------------- */

    @Test
    public void test_getEstablishment_ValidId_ReturnsEstablishment() {
        given()
                .when()
                .get(getBaseURI() + "/establishment/" + VERIFIED_EST_ID)
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", equalTo(VERIFIED_EST_ID))
                .body("name", equalTo(VERIFIED_EST_NAME))
                .body("aliases[0]", equalTo(VERIFIED_EST_ALIAS))
                // Validate categories
                .body("categories[0]", equalTo(CATEGORY_NAME));
    }

    @Test
    public void test_getEstablishment_InvalidId_ReturnsNotFound() {
        given()
                .when()
                .get(getBaseURI() + "/establishment/" + NON_EXISTENT_EST_ID)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    /* ----------------- getEstablishmentByRorId ----------------- */

    @Test
    public void test_getEstablishmentByRorId_ValidRorIdSuffix_ReturnsEstablishment() {
        String rorIdSuffix = VERIFIED_ROR_ID.replace("https://ror.org/", "");

        given()
                .when()
                .get(getBaseURI() + "/establishment/ror/" + rorIdSuffix)
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.OK.getStatusCode())
                .body("rorID", equalTo("https://ror.org/" + rorIdSuffix))
                .body("id", equalTo(VERIFIED_EST_ID))
                .body("name", equalTo(VERIFIED_EST_NAME));
    }

    @Test
    public void test_getEstablishmentByRorId_InvalidSuffix_ReturnsNull() {
        String invalidRorSuffix = "nonexistent123";

        given()
                .when()
                .get(getBaseURI() + "/establishment/ror/" + invalidRorSuffix)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    /* ----------------- getEstablishmentsByQuery ----------------- */

    @Test
    public void test_getEstablishmentsByQuery_OnlyVerifiedFalse_ReturnsUnverifiedEstablishment() {
        given().contentType("application/json")
                .body(Json.createObjectBuilder()
                        .add("name", UNVERIFIED_EST_NAME)
                        .build().toString())
                .queryParam("onlyVerified", false)
                .when()
                .post(getBaseURI() + "/establishment/search")
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.OK.getStatusCode())
                .body("name", hasItem(UNVERIFIED_EST_NAME));
    }

    @Test
    public void test_getEstablishmentsByQuery_OnlyVerifiedTrue_DoesNotReturnUnverifiedEstablishment() {
        given().contentType("application/json")
                .body(Json.createObjectBuilder()
                        .add("name", UNVERIFIED_EST_NAME)
                        .build().toString())
                .when()
                .post(getBaseURI() + "/establishment/search")
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.OK.getStatusCode())
                .body("name", not(hasItem(UNVERIFIED_EST_NAME)));
    }

    @Test
    public void test_getEstablishmentsByQuery_EstablishmentNonExistent_ReturnsEmptyList() {
        given().contentType("application/json")
                .body(Json.createObjectBuilder()
                        .add("name", UNVERIFIED_EST_NAME)
                        .build().toString())
                .when()
                .post(getBaseURI() + "/establishment/search")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("name", not(hasItem("NON_EXISTENT")));
    }

    @Test
    public void test_getEstablishmentsByQuery_MissingQueryParam_ReturnsBadRequest() {
        given().contentType("application/json")
                .when()
                .post(getBaseURI() + "/establishment/search")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void test_getEstablishmentsByQuery_WithAliasEnabled_ReturnsEstablishment() {
        given().contentType("application/json")
                .body(Json.createObjectBuilder()
                        .add("name", VERIFIED_EST_ALIAS)
                        .build().toString())
                .when()
                .queryParam("useAlias", true)
                .when()
                .post(getBaseURI() + "/establishment/search")
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.OK.getStatusCode())
                .body("name", hasItem(VERIFIED_EST_NAME));
    }

    @Test
    public void test_getEstablishmentsByQuery_WithAliasDisabled_DoesNotReturnEstablishment() {
        given().contentType("application/json")
                .body(Json.createObjectBuilder()
                        .add("name", VERIFIED_EST_ALIAS)
                        .build().toString())
                .queryParam("useAlias", false)
                .when()
                .post(getBaseURI() + "/establishment/search")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("name", not(hasItem(VERIFIED_EST_NAME)));
    }

    /* -----------------  getUnverifiedEstablishments ----------------- */

    @Test
    public void test_getUnverifiedEstablishments_ReturnsUnverifiedEstablishmentList() {
        given()
                .when()
                .get(getBaseURI() + "/establishment/unverified")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("name", hasItem(UNVERIFIED_EST_NAME));
    }


    /* -----------------  createUnverifiedEstablishment ----------------- */

    @Test
    public void test_createUnverifiedEstablishment_ValidInput_ReturnsCreatedEstablishment() {
        given()
                .contentType("application/json")
                .body(Json.createObjectBuilder()
                        .add("estName", NEW_EST_NAME)
                        .add("country", "TEST_COUNTRY")
                        .build().toString())
                .when()
                .post(getBaseURI() + "/establishment")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .body("name", equalTo(NEW_EST_NAME))
                .body("country", equalTo("TEST_COUNTRY"))
                .body("verified", equalTo(false));
    }

    @Test
    public void test_createUnverifiedEstablishment_EmptyName_ReturnsBadRequest() {
        given().contentType("application/json")
                .body(Json.createObjectBuilder()
                        .add("estName", "")
                        .add("country", "TEST_COUNTRY")
                        .build().toString())
                .when()
                .post(getBaseURI() + "/establishment")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    /* -----------------  getRorMatches ----------------- */

    @Test
    public void test_getRorMatches_ValidQuery_ReturnsMatches() {
        given()
                .queryParam("searchQuery", ROR_QUERY)
                .when()
                .get(getBaseURI() + "/establishment/ror-search")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("[0].names.find { it.types.contains('ror_display') }.value",
                        equalTo("King's College London"));
    }

    @Test
    public void test_getRorMatches_MissingQuery_ReturnsBadRequest() {
        given()
                .when()
                .get(getBaseURI() + "/establishment/ror-search")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    /* -----------------  rorVerifyAndEnrichData ----------------- */


    @Test
    public void test_rorVerifyAndEnrichData_ValidInput_ReturnsUpdatedEstablishment() {
        String RorTestPayLoad = RorPayloadBuilder.buildTestRorPayload();

        given()
                .contentType("application/json")
                .body(RorTestPayLoad)
                .when()
                .put(getBaseURI() + "/establishment/" + UNVERIFIED_EST_ID + "/enrich-verify/ror")
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.OK.getStatusCode())
                // Validate establishment
                .body("id", equalTo(UNVERIFIED_EST_ID))
                .body("name", equalTo(ROR_PAYLOAD_NAME))
                .body("rorID", equalTo(ROR_PAYLOAD_ROR_ID))
                .body("country", equalTo(ROR_PAYLOAD_COUNTRY))
                .body("url", equalTo(ROR_PAYLOAD_URL))
                .body("verified", equalTo(true))
                // Validate aliases
                .body("aliases", hasItems(ROR_PAYLOAD_LABEL, ROR_PAYLOAD_ACRONYM, ROR_PAYLOAD_ALIAS))
                // Validate categories
                .body("categories", hasItems(ROR_PAYLOAD_TYPE_1,ROR_PAYLOAD_TYPE_2));
    }

    @Test
    public void test_rorVerifyAndEnrichData_EstablishmentNonExistent_ReturnsNotFound() {
        String RorTestPayLoad = RorPayloadBuilder.buildTestRorPayload();

        given()
                .contentType("application/json")
                .body(RorTestPayLoad)
                .when()
                .put(getBaseURI() + "/establishment/" + NON_EXISTENT_EST_ID + "/enrich-verify/ror")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    /* -----------------  manualVerifyAndEnrichData ----------------- */

    @Test
    public void test_manualVerifyAndEnrichData_ValidInput_ReturnsUpdatedEstablishment() {
        String payload = Json.createObjectBuilder()
                .add("name", ROR_PAYLOAD_NAME)
                .add("rorID", ROR_PAYLOAD_ROR_ID)
                .add("country", ROR_PAYLOAD_COUNTRY)
                .add("url", ROR_PAYLOAD_URL)
                .build()
                .toString();

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .put(getBaseURI() + "/establishment/" + UNVERIFIED_EST_ID+ "/enrich-verify")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", equalTo(UNVERIFIED_EST_ID))
                .body("name", equalTo(ROR_PAYLOAD_NAME))
                .body("rorID", equalTo(ROR_PAYLOAD_ROR_ID))
                .body("country", equalTo(ROR_PAYLOAD_COUNTRY))
                .body("url", equalTo(ROR_PAYLOAD_URL))
                .body("verified", equalTo(true));
    }

    @Test
    public void test_manualVerifyAndEnrichData_MissingInput_ReturnsBadRequest() {
        given()
                .contentType("application/json")
                .when()
                .put(getBaseURI() + "/establishment/" + UNVERIFIED_EST_ID + "/enrich-verify")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    /* -----------------  addEstablishmentAliases ----------------- */

    @Test
    public void test_addEstablishmentAliases_ValidInput_ReturnsUpdatedAliases() {
        given()
                .contentType("application/json")
                .body(Json.createArrayBuilder()
                        .add(NEW_ALIAS_NAME_1)
                        .add(NEW_ALIAS_NAME_2)
                        .build()
                        .toString())
                .when()
                .put(getBaseURI() + "/establishment/" + VERIFIED_EST_ID + "/aliases")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("aliases", hasItems(NEW_ALIAS_NAME_1, NEW_ALIAS_NAME_2))
                .body("id", equalTo(VERIFIED_EST_ID));
    }

    @Test
    public void test_addEstablishmentAliases_AliasAlreadyExists_AliasNotAdded() {
        given()
                .contentType("application/json")
                .body(Json.createArrayBuilder()
                        .add(VERIFIED_EST_ALIAS)
                        .build()
                        .toString())
                .when()
                .put(getBaseURI() + "/establishment/" + VERIFIED_EST_ID + "/aliases")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    public void test_addEstablishmentAliases_EstablishmentNonExistent_ReturnsNotFound() {
        given()
                .contentType("application/json")
                .body(Json.createArrayBuilder()
                        .add(NEW_ALIAS_NAME_1)
                        .add(NEW_ALIAS_NAME_2)
                        .build()
                        .toString())
                .when()
                .put(getBaseURI() + "/establishment/" + NON_EXISTENT_EST_ID + "/aliases")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void test_addEstablishmentAliases_MissingInput_ReturnsBadRequest() {
        given()
                .contentType("application/json")
                .when()
                .put(getBaseURI() + "/establishment/" + VERIFIED_EST_ID + "/aliases")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    /* -----------------  addEstablishmentCategoryLinks ----------------- */

    @Test
    public void test_addEstablishmentCategoryLinks_ValidInput_ReturnsUpdatedCategories() {
        given()
                .contentType("application/json")
                .body("[-400, -500]")
                .when()
                .put(getBaseURI() + "/establishment/" + VERIFIED_EST_ID + "/categories")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("categories", hasItems(CATEGORY_NAME_2, CATEGORY_NAME_3))
                .body("id", equalTo(VERIFIED_EST_ID));
    }

    @Test
    public void test_addEstablishmentCategoryLinks_CategoryAlreadyExists_CategoryNotAdded() {
        given()
                .contentType("application/json")
                .body(Collections.singletonList(CATEGORY_ID))
                .when()
                .put(getBaseURI() + "/establishment/" + VERIFIED_EST_ID + "/categories")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    public void test_addEstablishmentCategoryLinks_EstablishmentNonExistent_ReturnsNotFound() {
        given()
                .contentType("application/json")
                .body("[1,2, 7]")
                .when()
                .put(getBaseURI() + "/establishment/" + NON_EXISTENT_EST_ID + "/categories")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void test_addEstablishmentCategoryLinks_MissingInput_ReturnsBadRequest() {
        given()
                .contentType("application/json")
                .when()
                .put(getBaseURI() + "/establishment/" + VERIFIED_EST_ID + "/categories")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    /* -----------------  deleteEstablishmentAndLinkedDepartments ----------------- */

    @Test
    public void test_deleteEstablishmentAndLinkedDepartments_ValidId_ReturnsSuccessMessage() {
        given()
                .when()
                .delete(getBaseURI() + "/establishment/" + VERIFIED_EST_ID)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void test_deleteEstablishmentAndLinkedDepartments_EstablishmentNonExistent_ReturnsNotFound() {
        given()
                .when()
                .delete(getBaseURI() + "/establishment/" + NON_EXISTENT_EST_ID)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    /* -----------------  getCountries ----------------- */

    @Test
    public void test_getUCountries_ReturnsCountriesList() {
        given()
                .when()
                .get(getBaseURI() + "/establishment/countries")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("name", hasItem("TEST_COUNTRY_1"))
                .body("name", hasItem("TEST_COUNTRY_2"));
    }

}
