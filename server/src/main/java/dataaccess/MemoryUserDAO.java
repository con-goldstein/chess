package dataaccess;
import java.util.*;
import Requests.RegisterRequest;
import model.*;
import java.util.UUID;
import spark.Response;

import javax.xml.crypto.Data;

public class MemoryUserDAO implements UserDAO {
    private final HashMap<String, UserData> userDatabase = new HashMap<>();
    private final HashMap<String, AuthData> authDatabase = new HashMap<>();

    public boolean findUser(RegisterRequest r) {
        //null if user
        if (userDatabase.get(r.username()) != null){
            System.out.println("user found");
            return true;
        }
        System.out.println("user not found");
        return false;
    }

    public void createUser(RegisterRequest r, Response res) throws DataAccessException {
        try{
        // if one of the fields is empty
            //add user to database
            userDatabase.put(r.username(), new UserData(r.username(), r.password(), r.email()));
            System.out.println("user added to userDatabase");
            System.out.println(userDatabase.get(r.username()));
        }
        catch (Exception e){
            res.status(500);
            throw new DataAccessException("error creating user");

        }
    }
    public String addAuthToken(RegisterRequest r){
        //create authToken
        String authToken = createAuth();
        //add user data into database
        AuthData authData = new AuthData(authToken, r.username());
        authDatabase.put(r.username(), authData);
        return authToken;
    }


    public String createAuth(){
        return UUID.randomUUID().toString();
    }
}
