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

    @Test
    public void test_getEstablishmentsByQuery() {
        given()
                .queryParam("searchQuery", "TESTox")
                .when()
                .get(getBaseURI() + "/establishment/search")
                .then()
                .statusCode(200)
                .body("establishmentName", hasItem("TESTox"));
    }

}
