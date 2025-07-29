package service;
import dataaccess.*;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import exceptions.UnauthorizedException;
import requests.*;
import results.*;

import java.util.UUID;

public class UserService {
    public static LoginResult login(LoginRequest loginRequest, UserDAO user, AuthDAO auth)
            throws BadRequestException, UnauthorizedException, DataAccessException {
        if (loginRequest.username() == null || loginRequest.password() == null){
            throw new BadRequestException("Missing username or password");
        }
        boolean userFound = user.findUser(loginRequest.username());
        if (!userFound){
            throw new UnauthorizedException("User not found");
        }
        //user found, check if passwords match
        boolean match = user.match(loginRequest.username(), loginRequest.password());
        if (match){
            String authToken = UUID.randomUUID().toString();
            auth.addAuthToken(loginRequest.username(), authToken);
            return new LoginResult(loginRequest.username(), authToken);
        }
        else{
            throw new UnauthorizedException("Passwords do not match");
        }
    }
    public static RegisterResult register(RegisterRequest registerRequest, UserDAO user, AuthDAO auth)
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
             user.createUser(registerRequest);

            //add authToken and add to database
            String authToken = UUID.randomUUID().toString();
            auth.addAuthToken(registerRequest.username(), authToken);
            return new RegisterResult(registerRequest.username(), authToken);
        }
        else{
            //user is found
            //change status response to 403
            throw new AlreadyTakenException("username already taken");
        }
    }

    public static Object logout(LogoutRequest logoutRequest, AuthDAO authDAO)
    throws UnauthorizedException, DataAccessException{
        boolean foundToken = authDAO.findAuthToken(logoutRequest.authToken());

        if (!foundToken){
            throw new UnauthorizedException("Authentication Token not found");
        }
        System.out.println("found authToken");
        authDAO.delete(logoutRequest.authToken());
        return "{}";
    }
}
