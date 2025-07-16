package dataaccess;

import model.AuthData;

public interface AuthDAO {
    String addAuthToken(String username);
    void clear();
    boolean findAuthToken(String authToken);
    void delete(String authToken);
    AuthData getauthData(String authToken);
    }
