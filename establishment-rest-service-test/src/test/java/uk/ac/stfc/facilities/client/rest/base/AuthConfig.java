package uk.ac.stfc.facilities.client.rest.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class AuthConfig {
    private static final String CONFIG_PATH = "uk.stfc.bisapps.config";
    private static final String USER_OFFICE_TOKEN = "USER_OFFICE_TOKEN";
    private static final String USER_TOKEN = "USER_TOKEN";
    private static final List<String> TOKEN_NAME_LIST = List.of(USER_OFFICE_TOKEN, USER_TOKEN);
    private static Properties restTestConfigProperties = null;

    public AuthConfig() {
        int unsetEnvs = 0;
        for (String token : TOKEN_NAME_LIST) {
            if (System.getenv(token) == null) {
                unsetEnvs++;
            }
        }
        if (unsetEnvs > 0) {
            String configPath = System.getProperty(CONFIG_PATH);
            if (configPath == null) {
                throw new RuntimeException("Rest Test Config is not defined as an Environment Variable, or a File");
            }
            File restTestConfig = new File(configPath);
            if (restTestConfig.isFile()) {
                try {
                    restTestConfigProperties = new Properties();
                    restTestConfigProperties.load(new FileInputStream(restTestConfig));
                } catch (IOException e) {
                    throw new RuntimeException("Rest Test Config is not defined as an Environment Variable, or a File: " + e);
                }
            } else {
                throw new RuntimeException("Rest Test Config is not defined as an Environment Variable, or a File");
            }
        }
    }

    public String getUserOfficeToken() {
        String userOfficeToken = System.getenv(USER_OFFICE_TOKEN);
        if (userOfficeToken != null) {
            return userOfficeToken;
        }
        return restTestConfigProperties.getProperty(USER_OFFICE_TOKEN);
    }

    public String getUserToken() {
        String userToken = System.getenv(USER_TOKEN);
        if (userToken != null) {
            return userToken;
        }
        return restTestConfigProperties.getProperty(USER_TOKEN);
    }
}
