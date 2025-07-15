package dataaccess;
import spark.Response;
import Requests.*;

import javax.xml.crypto.Data;

public interface UserDAO{
    void createUser(RegisterRequest registerRequest, Response res) throws DataAccessException;
    boolean findUser(String username);
    boolean match(String username, String password);
    void clear();
}
