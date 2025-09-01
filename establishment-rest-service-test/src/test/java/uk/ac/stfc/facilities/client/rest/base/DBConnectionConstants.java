package uk.ac.stfc.facilities.client.rest.base;

import io.github.cdimascio.dotenv.Dotenv;

public class DBConnectionConstants {
    private static DBConnectionConstants instance;
    private String databaseUrl, userDBUsername, userDBPassword;

    private DBConnectionConstants() {
        Dotenv dotenv = Dotenv.load();
        this.databaseUrl = dotenv.get("DB_URL");
        this.userDBUsername = dotenv.get("DB_USERNAME");
        this.userDBPassword = dotenv.get("DB_PASSWORD");
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }
    public String getUserDBUsername() {
        return userDBUsername;
    }
    public String getUserDBPassword() {
        return userDBPassword;
    }
    public static DBConnectionConstants getInstance(){
        if (instance == null) {
            instance = new DBConnectionConstants();
        }
        return instance;

    }
}