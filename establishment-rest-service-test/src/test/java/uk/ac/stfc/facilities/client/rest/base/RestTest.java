package uk.ac.stfc.facilities.client.rest.base;

public abstract class RestTest extends RestTestDatabaseClient {
    public static String BASE_URI_PROPERTY = "userOfficeTest.restUri";
    public static String getBaseURI() {
        return System.getProperty(BASE_URI_PROPERTY);
    }
}
