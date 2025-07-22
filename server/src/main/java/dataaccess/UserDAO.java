package dataaccess;
import org.mindrot.jbcrypt.BCrypt;
import requests.*;

public interface UserDAO{
    void createUser(RegisterRequest registerRequest) throws DataAccessException;
    boolean findUser(String username) throws DataAccessException;
    boolean match(String username, String password) throws DataAccessException;
    void clear() throws DataAccessException;

    default String createHashedPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    default boolean checkHashedPassword(String hashedPassword, String providedClearTextPassword) {
        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
    }



    }
