package dataaccess;

import exceptions.DataAccessException;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static String databaseName;
    private static String dbUsername;
    private static String dbPassword;
    private static String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        loadPropertiesFromResources();
    }

    /**
     * Creates the database if it does not already exist.
     */
    static public void createDatabase() throws DataAccessException {
        var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database", ex);
        }
    }

    static public void createGameDatabase() throws DataAccessException, SQLException {
        try (var conn = getConnection()) {

            var useDatabase = "USE " + databaseName;
            try (var usingDatabase = conn.prepareStatement(useDatabase);){
                usingDatabase.executeUpdate();
            }
            //create game table
            var createGameTable = """
                            CREATE TABLE IF NOT EXISTS game ( 
                            gameID int NOT NULL,
                            whiteUsername varchar(255),
                            blackUsername varchar(255),
                            gameName varchar(255) NOT NULL PRIMARY KEY,
                            ChessGame longtext NOT NULL
                            )""";
            try (var createdTableStatement = conn.prepareStatement(createGameTable)){
                createdTableStatement.executeUpdate();
            }
        }
    }

    static public void createUserDatabase() throws DataAccessException, SQLException {
        try (var conn = getConnection()) {

            var useDatabase = "USE " + databaseName;
            try (var usingDatabase = conn.prepareStatement(useDatabase);){
                usingDatabase.executeUpdate();
            }
        //create auth table
            var createUserTable = """
                            CREATE TABLE IF NOT EXISTS user ( 
                            username varchar(255) NOT NULL,
                            password varchar(255) NOT NULL,
                            email varchar(255) NOT NULL
                            )""";
            try (var createdTableStatement = conn.prepareStatement(createUserTable)){
                createdTableStatement.executeUpdate();
            }
        }
    }

    static public void createAuthDatabase() throws DataAccessException, SQLException{
        try (var conn = getConnection()) {

            var useDatabase = "USE " + databaseName;
            try (var usingDatabase = conn.prepareStatement(useDatabase);){
                usingDatabase.executeUpdate();
            }
            //create auth table
            var createAuthTable = """
                            CREATE TABLE IF NOT EXISTS auth ( 
                            username varchar(255) NOT NULL,
                            authToken varchar(255) NOT NULL  
                            )""";
            try (var createdTableStatement = conn.prepareStatement(createAuthTable)){
                createdTableStatement.executeUpdate();
            }
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DatabaseManager.getConnection()) {
     * // execute SQL statements.
     * }
     * </code>
     */
    //made getConnection public
    public static Connection getConnection() throws DataAccessException {
        try {
            //do not wrap the following line with a try-with-resources
            var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get connection", ex);
        }
    }

    private static void loadPropertiesFromResources() {
        try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
            if (propStream == null) {
                throw new Exception("Unable to load db.properties");
            }
            Properties props = new Properties();
            props.load(propStream);
            loadProperties(props);
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties", ex);
        }
    }

    private static void loadProperties(Properties props) {
        databaseName = props.getProperty("db.name");
        dbUsername = props.getProperty("db.user");
        dbPassword = props.getProperty("db.password");

        var host = props.getProperty("db.host");
        var port = Integer.parseInt(props.getProperty("db.port"));
        connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
    }
}
