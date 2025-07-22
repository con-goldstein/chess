package dataaccess;

import model.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static dataaccess.DatabaseManager.createDatabase;

public class SQLAuthDAO implements AuthDAO{

    public SQLAuthDAO() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        DatabaseManager.createAuthDatabase();
    }

    public void addAuthToken(String username, String authToken){
        try (var conn = DatabaseManager.getConnection()){
            var statement = "INSERT INTO auth (username, authToken) VALUES(?, ?)";
            var prepstatement = conn.prepareStatement(statement);
            prepstatement.setString(1, username);
            prepstatement.setString(2, authToken);
            prepstatement.executeUpdate();
        }
        catch (SQLException | DataAccessException e){
            e.printStackTrace();
        }
    }

    public void clear(){
        try (var conn = DatabaseManager.getConnection()){
            var statement = "TRUNCATE TABLE auth";
            var prepstatement = conn.prepareStatement(statement);
            prepstatement.executeUpdate();
        }
        catch (SQLException | DataAccessException e){}
    }


    public boolean findAuthToken(String authToken){
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT authToken FROM auth WHERE authToken=?";
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authToken);
            try (var rs = preparedStatement.executeQuery()){
                return rs.next();
            }
        }
        catch (SQLException | DataAccessException e){
            return false;
        }
    }

    public void delete(String authToken){
        try (var conn = DatabaseManager.getConnection()){
            var statement = "DELETE FROM auth WHERE authToken=?";
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        }catch (SQLException | DataAccessException e){
        }
    }

    public AuthData getauthData(String authToken){
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM auth WHERE authToken=?";
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authToken);
            try (var rs = preparedStatement.executeQuery()){
                if (rs.next()){
                    String username = rs.getString("username");
                    return new AuthData(authToken, username);
                }
                else{
                    return null;
                }
            }
        }
        catch (SQLException | DataAccessException e) {
            return null;
        }
    }
    public String createAuth(){
        return UUID.randomUUID().toString();
    }
}
