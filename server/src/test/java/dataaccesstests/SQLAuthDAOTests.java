package dataaccesstests;
import dataaccess.*;
import model.AuthData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.opentest4j.AssertionFailedError;
import requests.*;

import java.sql.SQLException;
import java.util.UUID;

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
        String authToken = UUID.randomUUID().toString();
        authDAO.addAuthToken(username, authToken);
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
            String authToken = UUID.randomUUID().toString();
            authDAO.addAuthToken("username", authToken);
            authDAO.addAuthToken("username", authToken);
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

    @Test public void goodfindAuthTokenTests() throws DataAccessException{
        String authToken = UUID.randomUUID().toString();
        authDAO.addAuthToken("Connor", authToken);
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT authToken FROM auth WHERE username=?";
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, "Connor");
            try (var res = preparedStatement.executeQuery()){
                if (res.next()){
                    String dbToken = res.getString("authToken");
                    assertEquals(authToken, dbToken);
                }
                else{fail("no result returned");}
            }
        }
        catch (SQLException | DataAccessException e){}
    }

    @Test public void badfindAuthTokenTests() throws DataAccessException {
        boolean foundToken = authDAO.findAuthToken("badToken");
        assertFalse(foundToken);
    }


    @Test public void goodDeleteTest(){
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT authToken FROM auth WHERE username=?";
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, "username");
            try (var res = preparedStatement.executeQuery()){
                String authToken = res.getString("authToken");
                authDAO.delete(authToken);
                assertFalse(res.next());
            }
        }
        catch(SQLException | DataAccessException e){}
    }


    @Test public void badDeleteTest(){
        assertDoesNotThrow(() -> authDAO.delete("badauthToken"));
    }

    @Test public void goodGetAuthData() throws DataAccessException{
        String authToken = UUID.randomUUID().toString();
        authDAO.addAuthToken("Connor", authToken);
        AuthData data = authDAO.getauthData(authToken);
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM auth WHERE authToken=?";
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authToken);
            try (var res = preparedStatement.executeQuery()){
                if (res.next()){
                    String dbToken = res.getString("authToken");
                    String dbUsername = res.getString("username");
                    assertEquals(data.authToken(), dbToken);
                    assertEquals(data.username(), dbUsername);
                 }
                else{
                    fail("could not find correct AuthData");
                }
            }
        }
        catch (SQLException | DataAccessException e){
            fail("test failed");
        }
    }

    @Test public void badGetAuthData() throws DataAccessException{
        String authToken = UUID.randomUUID().toString();
        authDAO.addAuthToken("Connor", authToken);
        AuthData data = authDAO.getauthData("badToken");
        assertNull(data);
    }
}
