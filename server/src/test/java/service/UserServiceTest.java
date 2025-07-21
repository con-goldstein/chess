package service;

import requests.LoginRequest;
import requests.LogoutRequest;
import requests.RegisterRequest;
import results.RegisterResult;
import dataaccess.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

class UserServiceTest {
    private static UserDAO user;
    private static AuthDAO auth;
    private static GameDAO game;
    private static RegisterRequest request;


    @BeforeEach
    public void beforeAll() {
          user = new MemoryUserDAO();
          auth = new MemoryAuthDAO();
          game = new MemoryGameDAO();
         request = new RegisterRequest("username", "password", "email");

    }

    @Test
    //create new user
    public void goodRegisterTest() throws DataAccessException, BadRequestException, AlreadyTakenException{
        UserService.register(request, user, auth);
    }

    @Test
    //registering multiple people with the same username
    public void badRegisterTest() throws DataAccessException, BadRequestException, AlreadyTakenException{
        RegisterRequest duplicate = new RegisterRequest("username","password", "email");
        assertThrows(AlreadyTakenException.class, () ->{
            UserService.register(request, user, auth);
            UserService.register(duplicate, user, auth);
        });
    }

    @Test
    //registers and logs in user
    public void goodLoginTest() throws BadRequestException, UnauthorizedException, AlreadyTakenException,
            DataAccessException{
        LoginRequest loginRequest = new LoginRequest("username", "password");
        UserService.register(request, user, auth);
        UserService.login(loginRequest, user, auth);
    }

    @Test
    //Logs in user with wrong password
    public void badLoginTest() throws BadRequestException, UnauthorizedException, AlreadyTakenException,
            DataAccessException{
        assertThrows(UnauthorizedException.class, () ->{
            UserService.register(request, user, auth);
            UserService.login(new LoginRequest("username", "wrongPassword"), user, auth);
        });

    }

    @Test
    //successfully logs out user
    public void goodLogoutTest()throws BadRequestException, UnauthorizedException, AlreadyTakenException,
            DataAccessException{
        LoginRequest loginRequest = new LoginRequest("username", "password");
        RegisterResult result = UserService.register(request, user, auth);
        UserService.login(loginRequest, user, auth);
        String authToken = result.authToken();
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        UserService.logout(logoutRequest, auth);
    }

    @Test
    //wrong authToken, invalid logout test
    public void badLogoutTest() {
        assertThrows(UnauthorizedException.class, () -> {
            LoginRequest loginRequest = new LoginRequest("username", "password");
            RegisterResult result = UserService.register(request, user, auth);
            UserService.login(loginRequest, user, auth);
            String authToken = result.authToken();
            LogoutRequest logoutRequest = new LogoutRequest("wrong");
            UserService.logout(logoutRequest, auth);
        });
    }
}