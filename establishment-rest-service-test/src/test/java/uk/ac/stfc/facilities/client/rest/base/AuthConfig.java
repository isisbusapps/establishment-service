package uk.ac.stfc.facilities.client.rest.base;

import uk.stfc.bisapps.config.BISAppProperties;

public class AuthConfig {
    private static final String CONFIG_PATH = "uk.stfc.bisapps.config";
    private static final String SUPER_ADMIN_TOKEN = "SUPER_ADMIN_TOKEN";
    private static final String ADMIN_TOKEN = "ADMIN_TOKEN";
    private static final String USER_OFFICE_TOKEN = "USER_OFFICE_TOKEN";
    private static final String USER_TOKEN = "USER_TOKEN";
    private static BISAppProperties bisAppProperties = null;

    public AuthConfig(){
        bisAppProperties = new BISAppProperties(System.getProperty(CONFIG_PATH));
    }

    public String getSuperAdminToken() {
        return bisAppProperties.getProperty(SUPER_ADMIN_TOKEN);
    }

    public String getAdminToken() {
        return bisAppProperties.getProperty(ADMIN_TOKEN);
    }

    public String getUserOfficeToken() {
        return bisAppProperties.getProperty(USER_OFFICE_TOKEN);
    }

    public String getUserToken() {
        return bisAppProperties.getProperty(USER_TOKEN);
    }
}
