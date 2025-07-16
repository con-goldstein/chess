package dataaccess;
import java.util.*;
import requests.RegisterRequest;
import model.*;

public class MemoryUserDAO implements UserDAO {
    private final HashMap<String, UserData> userDatabase = new HashMap<>();
    public boolean findUser(String username) {
        //null if user
        if (userDatabase.get(username) != null){
            return true;
        }
        return false;
    }

    public boolean match(String username, String password){
        return userDatabase.get(username).password().equals(password);
    }

    public void createUser(RegisterRequest r) throws DataAccessException {
        try{
        // if one of the fields is empty
            //add user to database
            userDatabase.put(r.username(), new UserData(r.username(), r.password(), r.email()));
        }
        catch (Exception e){
            throw new DataAccessException("error creating user");
        }
    }

    public void clear(){
        userDatabase.clear();
    }


}
