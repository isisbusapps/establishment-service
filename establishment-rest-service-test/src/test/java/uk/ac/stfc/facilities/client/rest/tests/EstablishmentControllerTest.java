package uk.ac.stfc.facilities.client.rest.tests;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import uk.ac.stfc.facilities.client.rest.base.RestTest;
import uk.ac.stfc.facilities.client.rest.resources.EstablishmentData;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static uk.ac.stfc.facilities.client.rest.base.Constants.*;


public class EstablishmentControllerTest extends RestTest {
    @Override
    protected void injectData() throws Exception {
        injectData(EstablishmentData.data);
    }

    @Override
    protected void cleanupData() throws Exception {
        deleteTestData("ESTABLISHMENT_ALIAS", "ALIAS_ID");
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





}
