package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void addAuthToken(String username, String authToken) throws DataAccessException;
    void clear() throws DataAccessException;
    boolean findAuthToken(String authToken) throws DataAccessException;
    void delete(String authToken) throws DataAccessException;
    AuthData getauthData(String authToken) throws DataAccessException;
    }
