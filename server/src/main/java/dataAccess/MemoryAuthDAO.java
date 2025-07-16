package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> authDatabase = new HashMap<>();

    public String addAuthToken(String username){
        //create authToken
        String authToken = createAuth();
        //add user data into database
        AuthData authData = new AuthData(authToken, username);
        authDatabase.put(authToken, authData);
        return authToken;
    }


    public String createAuth(){
        return UUID.randomUUID().toString();
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
