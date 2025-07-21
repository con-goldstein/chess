package dataAccessTests;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.SQLUserDAO;
import dataaccess.UserDAO;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import requests.*;

import java.sql.SQLException;

public class SQLUserDaoTests {
    private static UserDAO userDAO;
    private static RegisterRequest request;

    @BeforeEach
    public void beforeALL() throws SQLException, DataAccessException {
        userDAO = new SQLUserDAO();
        request = new RegisterRequest("username", "password", "email");

    }
    @Test
    public void goodCreateuserTest() throws DataAccessException{
        userDAO.createUser(new RegisterRequest("username", "password", "@gmail.com"));
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM user WHERE username=?";
            var prepstatement = conn.prepareStatement(statement);
            prepstatement.setString(1, "username");
            var result = prepstatement.executeQuery();
            if (result.next()) {
                String username = result.getString("username");
                String pass = result.getString("password");
                String email = result.getString("email");
                assertEquals("username", username);
                assertEquals("password", pass);
                assertEquals("@gmail.com", email);
            }
        }
        catch (SQLException | DataAccessException e){
            throw new DataAccessException("could not create user");
        }
    }
}
