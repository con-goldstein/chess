package dataaccess;

import exceptions.DataAccessException;
import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> authDatabase = new HashMap<>();

    public void addAuthToken(String username, String authToken) throws DataAccessException {
        //create authToken
        //add user data into database
        AuthData authData = new AuthData(authToken, username);
        authDatabase.put(authToken, authData);
    }

    public void clear(){
        authDatabase.clear();
    }

    public boolean findAuthToken(String authToken){
        return (authDatabase.get(authToken) != null);
    }

    public void delete(String authToken){
            authDatabase.remove(authToken);
    }

    public AuthData getauthData(String authToken){
        return authDatabase.get(authToken);
    }
}
