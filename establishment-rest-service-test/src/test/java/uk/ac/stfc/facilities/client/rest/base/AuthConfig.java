package uk.ac.stfc.facilities.client.rest.base;

import uk.stfc.bisapps.config.BISAppProperties;

import java.util.List;

public class AuthConfig {
    private static final String CONFIG_PATH = "uk.stfc.bisapps.config";
    private static final String USER_OFFICE_TOKEN = "USER_OFFICE_TOKEN";
    private static final String USER_TOKEN = "USER_TOKEN";
    private static final List<String> TOKEN_NAME_LIST = List.of(USER_OFFICE_TOKEN, USER_TOKEN);
    private static BISAppProperties bisAppProperties = null;

    public AuthConfig() {
        boolean unsetEnvs = false;
        for (String token : TOKEN_NAME_LIST) {
            if (System.getenv(token) == null) {
                unsetEnvs = true;
            }
        }
        if (unsetEnvs) {
            String configPath = System.getProperty(CONFIG_PATH);
            if (configPath == null) {
                throw new RuntimeException("Missing config");
            }
            bisAppProperties = new BISAppProperties(configPath);
        }
    }

    public String getUserOfficeToken() {
        String userOfficeToken = System.getenv(USER_OFFICE_TOKEN);
        if (userOfficeToken != null) {
            return userOfficeToken;
        }
        return bisAppProperties.getProperty(USER_OFFICE_TOKEN);
    }

    public String getUserToken() {
        String userToken = System.getenv(USER_TOKEN);
        if (userToken != null) {
            return userToken;
        }
        return bisAppProperties.getProperty(USER_TOKEN);
    }
}
