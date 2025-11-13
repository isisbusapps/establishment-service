package uk.ac.stfc.facilities.client.rest.tests;

import io.restassured.filter.log.LogDetail;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import uk.ac.stfc.facilities.client.rest.base.RestTest;
import uk.ac.stfc.facilities.client.rest.resources.CountryData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static uk.ac.stfc.facilities.client.rest.base.Constants.*;


public class CountryControllerTest extends RestTest {
    @Override
    protected void injectData() throws Exception {
        injectData(CountryData.data);
    }

    @Override
    protected void cleanupData() throws Exception {
        deleteTestData("COUNTRY_NEW", "ID");
    }

    /* ----------------- getCountry ----------------- */

    @Test
    public void test_getCountry_ValidId_ReturnsCountry() {
        given()
                .when()
                .get(getBaseURI() + "/country/" + COUNTRY_ID_1)
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", equalTo(COUNTRY_ID_1))
                .body("name", equalTo(TEST_COUNTRY_1));
    }

    @Test
    public void test_getCountry_InvalidId_ReturnsNotFound() {
        given()
                .when()
                .get(getBaseURI() + "/country/" + NON_EXISTENT_COUNTRY_ID)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    /* -----------------  getCountries ----------------- */

    @Test
    public void test_getAllCountries_ReturnsCountriesList() {
        given()
                .when()
                .get(getBaseURI() + "/country")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("name", hasItem("TEST_COUNTRY_1"))
                .body("name", hasItem("TEST_COUNTRY_2"));
    }

}
