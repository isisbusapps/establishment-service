package uk.ac.stfc.facilities.client.rest.base;

public class DBConnectionConstants {
    private static DBConnectionConstants instance;
    private final String databaseUrl;
    private final String userDBUsername;
    private final String userDBPassword;

    private DBConnectionConstants() {
        this.databaseUrl = System.getProperty("db.url");
        this.userDBUsername = System.getProperty("db.username");
        this.userDBPassword = System.getProperty("db.password");
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