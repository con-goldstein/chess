package dataaccess;
import model.*;
import org.mindrot.jbcrypt.BCrypt;
import requests.RegisterRequest;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{
    public SQLUserDAO() throws DataAccessException, SQLException {
        try {
            //create database
            DatabaseManager.createDatabase();
            DatabaseManager.createUserDatabase();
        }
        catch(SQLException | DataAccessException e){
            throw new DataAccessException("could not create Database");
        }
    }
    public void createUser(RegisterRequest registerRequest) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            boolean userFound = findUser(registerRequest.username());
            if (!userFound) {
                String hashedPass = createHashedPassword(registerRequest.password());
                var statement = "INSERT INTO user (username, password, email) VALUES(?, ?, ?)";
                var preparedStatement = conn.prepareStatement(statement);
                preparedStatement.setString(1, registerRequest.username());
                preparedStatement.setString(2, hashedPass);
                preparedStatement.setString(3, registerRequest.email());
                preparedStatement.executeUpdate();
            }
            else {
                throw new DataAccessException("error creating user");
            }
        }
        catch (SQLException e){
            throw new DataAccessException("could not create user");
        }
    }

    public boolean findUser(String username) {
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM user WHERE username=?";
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, username);
            try (var rs = preparedStatement.executeQuery()) {
                return rs.next();
            }
        }
        catch (SQLException | DataAccessException e){
            return false;
        }
    }
    public boolean match(String username, String password){
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT password FROM user WHERE username=?";
            var prepstatement = conn.prepareStatement(statement);
            prepstatement.setString(1, username);
            try (var rs = prepstatement.executeQuery()){
                if (rs.next()){
                    String hashedPass = rs.getString("password");
                    return checkHashedPassword(hashedPass, password);
                }
                return false;
            }
        }
        catch (SQLException | DataAccessException e){
            return false;
        }
    }
    public void clear(){
        try (var conn = DatabaseManager.getConnection()){
            var statement = "TRUNCATE TABLE user";
            var prepstatement = conn.prepareStatement(statement);
            prepstatement.executeUpdate();
        }
        catch (SQLException | DataAccessException e){}
    }


    }

