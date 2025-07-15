package service;
import dataaccess.*;
import Requests.*;
import Results.*;
import spark.*;

public class UserService {
    public static LoginResult login(LoginRequest LoginRequest, Response res, UserDAO user) throws BadRequestException,
            UnauthorizedException{
        if (LoginRequest.username() == null || LoginRequest.password() == null){
            throw new BadRequestException("Missing username or password");
        }
        boolean userFound = user.findUser(LoginRequest.username());
        if (!userFound){
            throw new BadRequestException("User not found");
        }
        //user found, check if passwords match
        boolean match = user.match(LoginRequest.username(), LoginRequest.password());
        if (match){
            String authToken = user.addAuthToken(LoginRequest.username());
            return new LoginResult(LoginRequest.username(), authToken);
        }
        else{
            throw new UnauthorizedException("Passwords do not match");
        }
    }
    public static RegisterResult register(RegisterRequest registerRequest, Response res, UserDAO user)
            throws DataAccessException, AlreadyTakenException, BadRequestException {

        //username, password and email must not be null
        if (registerRequest.username() == null || registerRequest.password() == null
                || registerRequest.email() == null){
            throw new BadRequestException("Missing username, password, or email");
        }

        //true is user is found
        boolean userFound = user.findUser(registerRequest.username());
        //if no user is found:
        if (!userFound){
            //add user to database
             user.createUser(registerRequest, res);

            //add authToken and add to database
            String authToken = user.addAuthToken(registerRequest.username());
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
