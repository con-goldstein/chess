package dataaccess;
import exceptions.DataAccessException;
import requests.*;

public interface UserDAO{
    void createUser(RegisterRequest registerRequest) throws DataAccessException;
    boolean findUser(String username) throws DataAccessException;
    boolean match(String username, String password) throws DataAccessException;
    void clear() throws DataAccessException;



    }
