package service;
import dataaccess.*;
import Requests.*;
import Results.*;
import spark.*;

public class UserService {
    public static RegisterResult register(RegisterRequest registerRequest, Response res, UserDAO user)
            throws DataAccessException, AlreadyTakenException, BadRequestException {

        //username, password and email must not be null
        if (registerRequest.username() == null || registerRequest.password() == null
                || registerRequest.email() == null){
            throw new BadRequestException("Missing username, password, or email");
        }

        //true is user is found
        boolean userFound = user.findUser(registerRequest);
        //if no user is found:
        if (!userFound){
            //add user to database
             user.createUser(registerRequest, res);

            //add authToken and add to database
            String authToken = user.addAuthToken(registerRequest);
            return new RegisterResult(registerRequest.username(), authToken);
        }
        else{
            //user is found
            //change status response to 403
            throw new AlreadyTakenException("username already taken");
        }
    }


//    public LoginResult login(LoginRequest loginRequest){}
//    public void logout(LogoutRequest logoutRequest){}
}
