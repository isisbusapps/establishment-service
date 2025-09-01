package uk.ac.stfc.facilities.client.rest.base;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.*;

import static com.ninja_squad.dbsetup.Operations.sql;

public abstract class RestTestDatabaseClient {
    public static Connection connection = null;
    private final static DBConnectionConstants dbConnectionConstants = DBConnectionConstants.getInstance();
    private final static String DATABASE_URL = dbConnectionConstants.getDatabaseUrl();
    private final static String DATABASE_USERNAME = dbConnectionConstants.getUserDBUsername();
    private final static String DATABASE_PASSWORD = dbConnectionConstants.getUserDBPassword();

    @BeforeAll
    public static void connectDB() throws SQLException {
        connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
    }

    @AfterAll
    public static void disconnectDB() throws SQLException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new SQLException("Could not disconnect");
            }
        }
    }

    @BeforeEach
    void loadData() throws Exception {
        try {
            injectData();
        } catch (Exception e) {
            cleanupData();
            injectData();
        }
    }

    @AfterEach
    void teardownData() throws Exception {
        cleanupData();
    }

    protected abstract void injectData() throws Exception;

    protected abstract void cleanupData() throws Exception;

    public void injectData(Operation operation) {
        DriverManagerDestination dest = new DriverManagerDestination(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
        DbSetup dbSetup = new DbSetup(dest, operation);
        dbSetup.launch();
    }

    public void deleteTestData(String table, String field) throws SQLException {
        sql("DELETE FROM " + table + " WHERE " + field + " < 0").execute(connection, null);
    }

    public void deleteTestData(String table, String field, String value) throws SQLException {
        sql("DELETE FROM " + table + " WHERE " + field + " = '" + value + "'").execute(connection, null);
    }

    public int getQueryRowCount(String query) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet standardRS = statement.executeQuery(query);
        if (!standardRS.next()) {
            throw new SQLException();
        }
        return standardRS.getInt(1);
    }
}
