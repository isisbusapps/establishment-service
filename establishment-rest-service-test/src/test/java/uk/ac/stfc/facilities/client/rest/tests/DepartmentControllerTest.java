package uk.ac.stfc.facilities.client.rest.tests;

import io.restassured.filter.log.LogDetail;
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
        deleteTestData("LABEL_KEYWORD", "ID");
        deleteTestData("DEPARTMENT_LABEL_LINK", "DEPARTMENT_ID");
        deleteTestData("DEPARTMENT_LABEL_LINK", "DEPARTMENT_ID");
        deleteTestData("DEPARTMENT_LABEL_LINK", "LABEL_ID");
        deleteTestData("LABEL", "ID");
        deleteTestData("DEPARTMENT", "ID");
        deleteDepartmentLabelsByDepartmentName(NEW_DEPARTMENT_NAME);
        deleteTestData("DEPARTMENT", "DEPARTMENT_NAME", NEW_DEPARTMENT_NAME);


        deleteTestData("ESTABLISHMENT_ALIAS", "ESTABLISHMENT_ID");
        deleteTestData("ESTABLISHMENT_CATEGORY_LINK", "ESTABLISHMENT_ID");
        deleteTestData("CATEGORY", "ID");
        deleteTestData("ESTABLISHMENT_NEW", "ID");
        deleteTestData("ESTABLISHMENT_NEW", "ESTABLISHMENT_NAME", NEW_EST_NAME);
    }

    /* ----------------- getDepartment ----------------- */

    @Test
    public void test_getDepartment_ValidId_ReturnsDepartment() {
        given()
                .when()
                .get(getBaseURI() + "/department/" + TEST_DEPARTMENT_ID)
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", equalTo(TEST_DEPARTMENT_ID))
                .body("name", equalTo(TEST_DEPARTMENT_NAME))
                .body("labels[0]", equalTo(TEST_LABEL_NAME));
    }

    @Test
    public void test_getDepartment_InvalidId_ReturnsNotFound() {
        given()
                .when()
                .get(getBaseURI() + "/department/" + NON_EXISTENT_DEPARTMENT_ID)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    /* ----------------- addDepartmentLabelLinksManually ----------------- */

    @Test
    public void test_addDepartmentLabelLinksManually_ValidInput_ReturnsUpdatedLinks() {
        given().header(AUTH_USER_OFFICE_HEADER)
                .contentType("application/json")
                .body(Json.createArrayBuilder()
                        .add(NEW_LABEL_ID_1)
                        .add(NEW_LABEL_ID_2)
                        .build().toString())
                .when()
                .put(getBaseURI() + "/department/" + TEST_DEPARTMENT_ID + "/label")
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", equalTo(TEST_DEPARTMENT_ID))
                .body("labels", hasItems(NEW_LABEL_NAME_1, NEW_LABEL_NAME_2));
    }

    @Test
    public void test_addDepartmentLabelLinksManually_LinkAlreadyExists_LinkNotAdded() {
        given().header(AUTH_USER_OFFICE_HEADER)
                .contentType("application/json")
                .body(Json.createArrayBuilder()
                        .add(TEST_LABEL_ID)
                        .build().toString())
                .when()
                .put(getBaseURI() + "/department/" + TEST_DEPARTMENT_ID + "/label")
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.OK.getStatusCode())
                .body("labels", not(hasItems(TEST_LABEL_ID)));
    }

    @Test
    public void test_addDepartmentLabelLinksManually_DepartmentNonExistent_ReturnsNotFound() {
        given().header(AUTH_USER_OFFICE_HEADER)
                .contentType("application/json")
                .body(Json.createArrayBuilder()
                        .add(TEST_LABEL_ID)
                        .build().toString())
                .when()
                .put(getBaseURI() + "/department/" + NON_EXISTENT_DEPARTMENT_ID + "/label")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void test_addDepartmentLabelLinksManually_LabelNonExistent_ReturnsNotFound() {
        given().header(AUTH_USER_OFFICE_HEADER)
                .contentType("application/json")
                .body(Json.createArrayBuilder()
                        .add(NON_EXISTENT_LABEL_ID)
                        .build().toString())
                .when()
                .put(getBaseURI() + "/department/" + TEST_DEPARTMENT_ID + "/label")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    /* ----------------- addDepartmentLabelLinksAutomatically ----------------- */

    @Test
    public void test_addDepartmentLabelLinksAutomatically_ValidInput_ReturnsLinks() {
        given().header(AUTH_USER_OFFICE_HEADER)
                .when()
                .put(getBaseURI() + "/department/" + TEST_DEPARTMENT_ID + "/label")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("labels", hasItems(NEW_LABEL_NAME_1, NEW_LABEL_NAME_2));
    }

    @Test
    public void test_addDepartmentLabelLinksAutomatically_DepartmentNonExistent_ReturnsNotFound() {
        given().header(AUTH_USER_OFFICE_HEADER)
                .when()
                .put(getBaseURI() + "/department/" + NON_EXISTENT_DEPARTMENT_ID + "/label")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    /* ----------------- removeDepartmentLabelLink ----------------- */

    @Test
    public void test_removeDepartmentLabelLink_ValidInput_RemovesLink() {
        given().header(AUTH_USER_OFFICE_HEADER)
                .when()
                .delete(getBaseURI() + "/department/" + TEST_DEPARTMENT_ID + "/label/" + TEST_LABEL_ID)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("labels", not(hasItems(TEST_LABEL_NAME)));
    }

    @Test
    public void test_removeDepartmentLabelLink_NonExistent_ReturnsNotFound() {
        given().header(AUTH_USER_OFFICE_HEADER)
                .when()
                .delete(getBaseURI() + "/department/" + TEST_DEPARTMENT_ID + "/label/" + NON_EXISTENT_LABEL_ID)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    /* ----------------- createDepartmentAndDepLabelLinks ----------------- */

    @Test
    public void test_createDepartmentAndDepLabelLinks_ValidInput_ReturnsDepartmentAndLinks() {
        String payload = Json.createObjectBuilder()
                .add("name", NEW_DEPARTMENT_NAME)
                .add("establishmentId", VERIFIED_EST_ID)
                .build().toString();

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post(getBaseURI() + "/department")
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.OK.getStatusCode())
                .body("name", equalTo(NEW_DEPARTMENT_NAME))
                .body("estId", equalTo(VERIFIED_EST_ID))
                .body("labels", hasItems(NEW_LABEL_NAME_1, NEW_LABEL_NAME_2));
    }

    @Test
    public void test_createDepartmentAndDepLabelLinks_MissingInput_ReturnsBadRequest() {
        String payload = Json.createObjectBuilder()
                .add("name", NEW_DEPARTMENT_NAME)
                .build().toString();

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post(getBaseURI() + "/department")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void test_createDepartmentAndDepLabelLinks_NonExistentEstablishment_ReturnsNotFound() {
        String payload = Json.createObjectBuilder()
                .add("name", NEW_DEPARTMENT_NAME)
                .add("establishmentId", NON_EXISTENT_EST_ID)
                .build().toString();

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post(getBaseURI() + "/department")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    /* ----------------- deleteDepartmentAndDepLabelLinks ----------------- */

    @Test
    public void test_deleteDepartmentAndDepLabelLinks_ValidInput_ReturnsSuccessMessage() {
        given().header(AUTH_USER_OFFICE_HEADER)
                .when()
                .delete(getBaseURI() + "/department/" + TEST_DEPARTMENT_ID)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void test_deleteDepartmentAndDepLabelLinks_DepartmentNonExistent_ReturnsNotFound() {
        given().header(AUTH_USER_OFFICE_HEADER)
                .when()
                .delete(getBaseURI() + "/department/" + NON_EXISTENT_DEPARTMENT_ID)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

}


