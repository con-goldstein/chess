package dataAccessTests;
import dataaccess.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.opentest4j.AssertionFailedError;
import requests.*;

import java.sql.SQLException;

public class SQLAuthDAOTests {
    private AuthDAO authDAO;
    private UserDAO userDAO;
    @BeforeEach
    public void beforeEach()throws SQLException, DataAccessException{
        authDAO = new SQLAuthDAO();
        userDAO = new SQLUserDAO();
        authDAO.clear();
        userDAO.clear();
        userDAO.createUser(new RegisterRequest("username", "password", "email"));
    }

    @Test
    public void goodAddAuthTokenTest() throws DataAccessException, SQLException{
        String username = "username";
        String authToken = authDAO.addAuthToken(username);
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM auth WHERE username=?";
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, username);
            var result = preparedStatement.executeQuery();
            if (result.next()) {
                String givenToken = result.getString("authToken");
                String givenUsername = result.getString("username");
                assertEquals(username, givenUsername);
                assertEquals(authToken, givenToken);
            }
        }
        catch (SQLException | DataAccessException e){
            throw new DataAccessException("could not find authToken");
        }
    }


    @Test public void badAddAuthTokenTest() {
        assertThrows(AssertionFailedError.class, () -> {
            authDAO.addAuthToken("username");
            authDAO.addAuthToken("username");
            assertThrows(SQLException.class, () -> {try (var conn = DatabaseManager.getConnection()){
                var statement = "SELECT * FROM auth WHERE username=?";
                var preparedStatement = conn.prepareStatement(statement);
                preparedStatement.setString(1, "username");
                try (var res = preparedStatement.executeQuery()){
                    res.next();
                    res.next();
                }
            }});
        });
    }
}
