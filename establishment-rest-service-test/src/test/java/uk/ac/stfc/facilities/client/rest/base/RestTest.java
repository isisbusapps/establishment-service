package uk.ac.stfc.facilities.client.rest.base;

import io.restassured.http.Header;

public abstract class RestTest extends RestTestDatabaseClient {
    public static String BASE_URI_PROPERTY = "userOfficeTest.restUri";
    private static final AuthConfig authConfig = new AuthConfig();
    protected final static Header AUTH_SUPER_ADMIN_HEADER = new Header("Authorization", authConfig.getSuperAdminToken());
    protected final static Header AUTH_ADMIN_HEADER = new Header("Authorization", authConfig.getAdminToken());
    protected final static Header AUTH_USER_OFFICE_HEADER = new Header("Authorization", authConfig.getUserOfficeToken());
    protected final static Header AUTH_USER_HEADER = new Header("Authorization", authConfig.getUserToken());

    public static String getBaseURI() {
        return System.getProperty(BASE_URI_PROPERTY);
    }
}
