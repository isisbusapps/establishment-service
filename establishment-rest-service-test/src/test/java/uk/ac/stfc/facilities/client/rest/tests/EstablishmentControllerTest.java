package uk.ac.stfc.facilities.client.rest.tests;

import jakarta.json.Json;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import uk.ac.stfc.facilities.client.rest.base.RestTest;
import uk.ac.stfc.facilities.client.rest.resources.DepartmentData;
import uk.ac.stfc.facilities.client.rest.resources.EstablishmentData;
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
    }

    @Override
    protected void cleanupData() throws Exception {
        deleteTestData("DEPARTMENT_LABEL_LINK", "DEPARTMENT_ID");
        deleteTestData("DEPARTMENT", "ID");

        deleteTestData("ESTABLISHMENT_ALIAS", "ESTABLISHMENT_ID");
        deleteTestData("ESTABLISHMENT_CATEGORY_LINK", "ESTABLISHMENT_ID");
        deleteTestData("ESTABLISHMENT_NEW", "ID");
        deleteTestData("ESTABLISHMENT_NEW", "ESTABLISHMENT_NAME", NEW_EST_NAME);
    }

    /* ----------------- getEstablishmentsByQuery ----------------- */

    @Test
    public void test_getEstablishmentsByQuery_OnlyVerifiedFalse_ReturnsUnverifiedEstablishment() {
        given()
                .queryParam("searchQuery", UNVERIFIED_EST_NAME)
                .queryParam("onlyVerified", false)
                .when()
                .get(getBaseURI() + "/establishment/search")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("establishmentName", hasItem(UNVERIFIED_EST_NAME));
    }

    @Test
    public void test_getEstablishmentsByQuery_OnlyVerifiedTrue_DoesNotReturnUnverifiedEstablishment() {
        given()
                .queryParam("searchQuery",  UNVERIFIED_EST_NAME)
                .when()
                .get(getBaseURI() + "/establishment/search")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("establishmentName", not(hasItem(UNVERIFIED_EST_NAME)));
    }

    @Test
    public void test_getEstablishmentsByQuery_EstablishmentNonExistent_ReturnsEmptyList() {
        given()
                .queryParam("searchQuery", "NON_EXISTENT")
                .when()
                .get(getBaseURI() + "/establishment/search")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("establishmentName", not(hasItem("NON_EXISTENT")));
    }

    @Test
    public void test_getEstablishmentsByQuery_MissingQueryParam_ReturnsBadRequest() {
        given()
                .when()
                .get(getBaseURI() + "/establishment/search")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void test_getEstablishmentsByQuery_WithAliasEnabled_ReturnsEstablishment() {
        given()
                .queryParam("searchQuery", VERIFIED_EST_ALIAS)
                .queryParam("useAlias", true)
                .when()
                .get(getBaseURI() + "/establishment/search")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("establishmentName", hasItem(VERIFIED_EST_NAME));
    }

    @Test
    public void test_getEstablishmentsByQuery_WithAliasDisabled_DoesNotReturnEstablishment() {
        given()
                .queryParam("searchQuery", VERIFIED_EST_ALIAS)
                .queryParam("useAlias", false)
                .when()
                .get(getBaseURI() + "/establishment/search")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("establishmentName", not(hasItem(VERIFIED_EST_NAME)));
    }

    /* -----------------  getUnverifiedEstablishments ----------------- */

    @Test
    public void test_getUnverifiedEstablishments_ReturnsUnverifiedEstablishmentList() {
        given()
                .when()
                .get(getBaseURI() + "/establishment/unverified")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("establishmentName", hasItem(UNVERIFIED_EST_NAME));
    }


    /* -----------------  createUnverifiedEstablishment ----------------- */

    @Test
    public void test_createUnverifiedEstablishment_ValidInput_ReturnsCreatedEstablishment() {
        given()
                .contentType("application/json")
                .body( NEW_EST_NAME)
                .when()
                .post(getBaseURI() + "/establishment")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .body("establishmentName", equalTo(NEW_EST_NAME))
                .body("verified", equalTo(false));
    }

    @Test
    public void test_createUnverifiedEstablishment_EmptyName_ReturnsBadRequest() {
        given()
                .contentType("application/json")
                .body("")
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
                .put(getBaseURI() + "/establishment/" + UNVERIFIED_EST_ID + "/ror-enrich-verify")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                // Validate establishment
                .body("establishment.establishmentId", equalTo(UNVERIFIED_EST_ID))
                .body("establishment.establishmentName", equalTo(ROR_PAYLOAD_NAME))
                .body("establishment.rorId", equalTo(ROR_PAYLOAD_ROR_ID))
                .body("establishment.countryName", equalTo(ROR_PAYLOAD_COUNTRY))
                .body("establishment.establishmentUrl", equalTo(ROR_PAYLOAD_URL))
                .body("establishment.verified", equalTo(true))
                // Validate aliases
                .body("aliases[0].alias", equalTo(ROR_PAYLOAD_LABEL))
                .body("aliases[1].alias", equalTo(ROR_PAYLOAD_ACRONYM))
                .body("aliases[2].alias", equalTo(ROR_PAYLOAD_ALIAS))
                // Validate categories
                .body("categories[0].category.categoryName", equalTo(ROR_PAYLOAD_TYPE_1))
                .body("categories[1].category.categoryName", equalTo(ROR_PAYLOAD_TYPE_2))
                .body("categories[0].establishment.establishmentId", equalTo(UNVERIFIED_EST_ID))
                .body("categories[1].establishment.establishmentId", equalTo(UNVERIFIED_EST_ID));
    }

    @Test
    public void test_rorVerifyAndEnrichData_EstablishmentNonExistent_ReturnsNotFound() {
        String RorTestPayLoad = RorPayloadBuilder.buildTestRorPayload();

        given()
                .contentType("application/json")
                .body(RorTestPayLoad)
                .when()
                .put(getBaseURI() + "/establishment/" + NON_EXISTENT_EST_ID + "/ror-enrich-verify")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    /* -----------------  manualVerifyAndEnrichData ----------------- */

    @Test
    public void test_manualVerifyAndEnrichData_ValidInput_ReturnsUpdatedEstablishment() {
        String payload = Json.createObjectBuilder()
                .add("establishmentName", ROR_PAYLOAD_NAME)
                .add("rorId", ROR_PAYLOAD_ROR_ID)
                .add("countryName", ROR_PAYLOAD_COUNTRY)
                .add("establishmentUrl", ROR_PAYLOAD_URL)
                .build()
                .toString();

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .put(getBaseURI() + "/establishment/" + UNVERIFIED_EST_ID+ "/manual-enrich-verify")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("establishmentId", equalTo(UNVERIFIED_EST_ID))
                .body("establishmentName", equalTo(ROR_PAYLOAD_NAME))
                .body("rorId", equalTo(ROR_PAYLOAD_ROR_ID))
                .body("countryName", equalTo(ROR_PAYLOAD_COUNTRY))
                .body("establishmentUrl", equalTo(ROR_PAYLOAD_URL))
                .body("verified", equalTo(true));
    }

    @Test
    public void test_manualVerifyAndEnrichData_MissingInput_ReturnsBadRequest() {
        given()
                .contentType("application/json")
                .when()
                .put(getBaseURI() + "/establishment/" + UNVERIFIED_EST_ID + "/manual-enrich-verify")
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
                .body("alias", hasItems(NEW_ALIAS_NAME_1, NEW_ALIAS_NAME_2))
                .body("establishmentId", everyItem(equalTo(VERIFIED_EST_ID)));
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
                .statusCode(Response.Status.OK.getStatusCode())
                .body("alias", not(hasItems(VERIFIED_EST_ALIAS)));
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
                .body("[2, 7]")
                .when()
                .put(getBaseURI() + "/establishment/" + VERIFIED_EST_ID + "/categories")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("categoryId", hasItems(2, 7))
                .body("establishmentId", everyItem(equalTo(VERIFIED_EST_ID)));
    }

    @Test
    public void test_addEstablishmentCategoryLinks_CategoryAlreadyExists_CategoryNotAdded() {
        given()
                .contentType("application/json")
                .body(Collections.singletonList(VERIFIED_EST_CATEGORY_ID))
                .when()
                .put(getBaseURI() + "/establishment/" + VERIFIED_EST_ID + "/categories")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("categoryId", not(hasItems(VERIFIED_EST_CATEGORY_ID)));
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
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    public void test_deleteEstablishmentAndLinkedDepartments_EstablishmentNonExistent_ReturnsNotFound() {
        given()
                .when()
                .delete(getBaseURI() + "/establishment/" + NON_EXISTENT_EST_ID)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
}
