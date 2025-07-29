package dataaccess;

import exceptions.DataAccessException;
import model.*;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{

    public SQLAuthDAO() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        DatabaseManager.createAuthDatabase();
    }

    public void addAuthToken(String username, String authToken) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()){
            var statement = "INSERT INTO auth (username, authToken) VALUES(?, ?)";
            var prepstatement = conn.prepareStatement(statement);
            prepstatement.setString(1, username);
            prepstatement.setString(2, authToken);
            prepstatement.executeUpdate();
        }
        catch (SQLException e){
            throw new DataAccessException("unable to addAuthToken");
        }
    }

    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            var statement = "TRUNCATE TABLE auth";
            var prepstatement = conn.prepareStatement(statement);
            prepstatement.executeUpdate();
        }
        catch (SQLException e){
            throw new DataAccessException("unable to clear");
        }
    }


    public boolean findAuthToken(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT authToken FROM auth WHERE authToken=?";
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authToken);
            try (var rs = preparedStatement.executeQuery()){
                return rs.next();
            }
        }
        catch (SQLException e){
            throw new DataAccessException("unable to connect to server");
        }
    }

    public void delete(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            var statement = "DELETE FROM auth WHERE authToken=?";
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new DataAccessException("unable to connect to database");
        }
    }

    public AuthData getauthData(String authToken) throws DataAccessException {
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
        catch (SQLException e) {
            return null;
        }
    }
}
