package dataaccess;

import Requests.RegisterRequest;
import spark.Response;

public interface AuthDAO {
    String addAuthToken(String username);
    void clear();
    boolean findAuthToken(String authToken);
    void delete(String authToken);
}
