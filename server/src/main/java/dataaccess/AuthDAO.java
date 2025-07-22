package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void addAuthToken(String username, String authToken);
    void clear();
    boolean findAuthToken(String authToken);
    void delete(String authToken);
    AuthData getauthData(String authToken);
    }
