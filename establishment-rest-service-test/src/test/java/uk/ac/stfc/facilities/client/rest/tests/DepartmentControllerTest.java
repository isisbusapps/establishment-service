package uk.ac.stfc.facilities.client.rest.tests;

import jakarta.json.Json;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import uk.ac.stfc.facilities.client.rest.base.RestTest;
import uk.ac.stfc.facilities.client.rest.resources.DepartmentData;
import uk.ac.stfc.facilities.client.rest.resources.EstablishmentData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static uk.ac.stfc.facilities.client.rest.base.Constants.*;


public class DepartmentControllerTest extends RestTest {
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

    /* ----------------- addDepartmentLabelLinksManually ----------------- */

    @Test
    public void test_addDepartmentLabelLinksManually_ValidInput_ReturnsUpdatedLinks() {
        given()
                .contentType("application/json")
                .body(Json.createArrayBuilder()
                        .add(NEW_LABEL_ID_1)
                        .add(NEW_LABEL_ID_2)
                        .build().toString())
                .when()
                .put(getBaseURI() + "/department/" + TEST_DEPARTMENT_ID + "/add-label-manual")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("department.departmentId", everyItem(equalTo(TEST_DEPARTMENT_ID)))
                .body("label.labelId", hasItems(NEW_LABEL_ID_1, NEW_LABEL_ID_2));
    }

    @Test
    public void test_addDepartmentLabelLinksManually_LinkAlreadyExists_LinkNotAdded() {
        given()
                .contentType("application/json")
                .body(Json.createArrayBuilder()
                        .add(TEST_LABEL_ID)
                        .build().toString())
                .when()
                .put(getBaseURI() + "/department/" + TEST_DEPARTMENT_ID + "/add-label-manual")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("label.labelId", not(hasItems(TEST_LABEL_ID)));
    }

    @Test
    public void test_addDepartmentLabelLinksManually_MissingInput_ReturnsBadRequest() {
        given()
                .contentType("application/json")
                .when()
                .put(getBaseURI() + "/department/" + TEST_DEPARTMENT_ID + "/add-label-manual")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void test_addDepartmentLabelLinksManually_DepartmentNonExistent_ReturnsNotFound() {
        given()
                .contentType("application/json")
                .body(Json.createArrayBuilder()
                        .add(TEST_LABEL_ID)
                        .build().toString())
                .when()
                .put(getBaseURI() + "/department/" + NON_EXISTENT_DEPARTMENT_ID + "/add-label-manual")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }


}


