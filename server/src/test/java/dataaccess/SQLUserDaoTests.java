package dataaccess;
import exceptions.DataAccessException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mindrot.jbcrypt.BCrypt;
import requests.*;

import java.sql.SQLException;

public class SQLUserDaoTests {
    private UserDAO userDAO;
    private RegisterRequest request;

    @BeforeEach
    public void beforeEach() throws SQLException, DataAccessException {
        userDAO = new SQLUserDAO();
        userDAO.clear();
        request = new RegisterRequest("username", "password", "email");

    }

    @Test
    public void goodCreateuserTest() throws DataAccessException {
        userDAO.createUser(request);
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM user WHERE username=?";
            var prepstatement = conn.prepareStatement(statement);
            prepstatement.setString(1, "username");
            var result = prepstatement.executeQuery();
            if (result.next()) {
                String username = result.getString("username");
                String hashedPass = result.getString("password");
                boolean hashedPassword = checkHashedPassword(hashedPass, "password");
                String email = result.getString("email");
                assertEquals("username", username);
                assertTrue(hashedPassword);
                assertEquals("email", email);
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("could not create user");
        }
    }
    public boolean checkHashedPassword(String hashedPassword, String providedClearTextPassword) {
        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
    }

    @Test
    public void badCreateUserTest() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(new RegisterRequest("username", "password", "email"));
            userDAO.createUser(new RegisterRequest("username", "password", "email"));
        });
    }

    @Test
    public void goodFindUserTest() throws DataAccessException {
        userDAO.createUser(request);
        boolean found = userDAO.findUser("username");
        assertTrue(found);
    }

    @Test
    public void badFindUserTest() throws DataAccessException {
        userDAO.createUser(request);
        boolean found = userDAO.findUser("wrongUsername");
        assertFalse(found);
    }

    @Test
    public void goodMatchTest() throws DataAccessException {
        userDAO.createUser(request);
        boolean matched = userDAO.match(request.username(), request.password());
        assertTrue(matched);
    }

    @Test
    public void badMatchTest() throws DataAccessException {
        userDAO.createUser(request);
        boolean matched = userDAO.match(request.username(), "badPassword");
        assertFalse(matched);
    }

    @Test public void clearTest() throws DataAccessException, SQLException {
        userDAO.createUser(request);
        userDAO.clear();
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM user WHERE username=?";
            var prepStatement = conn.prepareStatement(statement);
            prepStatement.setString(1, request.username());
            try (var res = prepStatement.executeQuery()){
                assertFalse(res.next());
            }
        }
    }
}