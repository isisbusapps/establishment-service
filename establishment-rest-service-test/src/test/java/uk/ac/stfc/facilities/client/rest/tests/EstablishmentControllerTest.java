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
        deleteTestData("ESTABLISHMENT_NEW", "ID");
    }

    /* ----------------- Search / Query Endpoints ----------------- */

    @Test
    public void test_getEstablishmentsByQuery_EstablishmentExists_ReturnsEstablishmentList() {
        given()
                .queryParam("searchQuery", VERIFIED_EST_NAME)
                .when()
                .get(getBaseURI() + "/establishment/search")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("establishmentName", hasItem(VERIFIED_EST_NAME));
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
    public void test_getUnverifiedEstablishments() {
        given()
                .when()
                .get(getBaseURI() + "/establishment/unverified")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("establishmentName", hasItem(UNVERIFIED_EST_NAME));
    }




}
