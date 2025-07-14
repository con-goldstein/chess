package dataaccess;
import spark.Response;
import Requests.*;

import javax.xml.crypto.Data;

public interface UserDAO{
    boolean findUser(RegisterRequest registerRequest);

    void createUser(RegisterRequest registerRequest, Response res) throws DataAccessException;

    String addAuthToken(RegisterRequest registerRequest);
}
