package dataaccess;
import model.*;
import requests.RegisterRequest;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{
    public SQLUserDAO() throws DataAccessException, SQLException {
        try {
            //create database
            //do I need to create database before constructing every SQLDAO?
            DatabaseManager.createDatabase();
            DatabaseManager.createUserDatabase();
        }
        catch(SQLException | DataAccessException e){
            throw new DataAccessException("could not create Database");
        }
    }
    public void createUser(RegisterRequest registerRequest) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()){
            var statement = "INSERT INTO user (username, password, email) VALUES(?, ?, ?)";
            var prepstatement = conn.prepareStatement(statement);
            prepstatement.setString(1, registerRequest.username());
            prepstatement.setString(2, registerRequest.password());
            prepstatement.setString(3, registerRequest.email());
            prepstatement.executeUpdate();
        }
        catch (SQLException | DataAccessException e){
            throw new DataAccessException("could not create user");
        }
    }

public class MySQLUserDAO {
}
