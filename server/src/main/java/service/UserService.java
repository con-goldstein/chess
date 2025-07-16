package service;
import dataaccess.*;
import requests.*;
import results.*;

public class UserService {
    public static LoginResult login(LoginRequest Loginrequest, UserDAO user, AuthDAO auth)
            throws BadRequestException,
            UnauthorizedException{
        if (Loginrequest.username() == null || Loginrequest.password() == null){
            throw new BadRequestException("Missing username or password");
        }
        boolean userFound = user.findUser(Loginrequest.username());
        if (!userFound){
            throw new UnauthorizedException("User not found");
        }
        //user found, check if passwords match
        boolean match = user.match(Loginrequest.username(), Loginrequest.password());
        if (match){
            String authToken = auth.addAuthToken(Loginrequest.username());
            return new LoginResult(Loginrequest.username(), authToken);
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
            String authToken = auth.addAuthToken(registerRequest.username());
            return new RegisterResult(registerRequest.username(), authToken);
        }
        else{
            //user is found
            //change status response to 403
            throw new AlreadyTakenException("username already taken");
        }
    }

    public static Object logout(LogoutRequest logoutRequest, AuthDAO authDAO)
    throws UnauthorizedException{
        boolean foundToken = authDAO.findAuthToken(logoutRequest.authToken());

        if (!foundToken){
            throw new UnauthorizedException("Authentication Token not found");
        }
        System.out.println("found authToken");
        authDAO.delete(logoutRequest.authToken());
        return "{}";
    }
}
