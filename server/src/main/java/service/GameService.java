package service;

import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import exceptions.UnauthorizedException;
import requests.*;
import results.CreateResult;
import dataaccess.*;
import model.GameData;

import java.util.HashSet;

public class GameService {
//    public CreateResult create(CreateRrequest createRequest){}
//    public JoinResult join(JoinRequest joinRequest){}
    public static HashSet<GameData> list(String authToken, AuthDAO authDAO, GameDAO gameDAO)
            throws UnauthorizedException, DataAccessException {
        boolean foundToken = authDAO.findAuthToken(authToken);
        if (!foundToken){
            throw new UnauthorizedException("could not find authToken");
        }
        return gameDAO.findGames();
    }

    public static CreateResult create(CreateRequest createRequest, AuthDAO authDAO, GameDAO gameDAO)
    throws UnauthorizedException, BadRequestException, DataAccessException{
        boolean foundToken = authDAO.findAuthToken(createRequest.authToken());
        if (!foundToken){
            throw new UnauthorizedException("could not find authToken");
        }
        if ((createRequest.authToken() == null) || (createRequest.gameName() == null)){
            throw new BadRequestException("authToken or gameName required");
        }
        return gameDAO.createGameData(createRequest.gameName());
    }

    public static GameData join(String authToken, JoinRequest joinRequest, AuthDAO authDAO, GameDAO gameDAO)
            throws UnauthorizedException, BadRequestException, AlreadyTakenException, DataAccessException {

        boolean foundToken = authDAO.findAuthToken(authToken);
        if (!foundToken){
            throw new UnauthorizedException("could not find authToken");
        }
        int gameID = joinRequest.gameID();
        String playerColor = joinRequest.playerColor();
        GameData gameData = findGame(gameID, gameDAO);
        if (gameData == null){
            throw new BadRequestException("invalid gameID");
        }

        String username = authDAO.getauthData(authToken).username();
        if (playerColor == null){
            throw new BadRequestException("add player color");
        }
        //false if not null and usernames are different
        if (playerColor.equals("WHITE")){
            if (gameData.whiteUsername() == null || gameData.whiteUsername().equals(username)){
                GameData newGameData = new GameData(gameID, username, gameData.blackUsername(),
                        gameData.gameName(), gameData.game());
                gameDAO.addGame(newGameData);
                return newGameData;
            }
            else{
                throw new AlreadyTakenException("color already taken");}
        }
        else if (playerColor.equals("BLACK")) {
            if(gameData.blackUsername() == null || gameData.blackUsername().equals(username)){
                GameData newGameData = new GameData(gameID, gameData.whiteUsername(), username,
                        gameData.gameName(), gameData.game());
                gameDAO.addGame(newGameData);
                return newGameData;
            }
            else{
                throw new AlreadyTakenException("color already taken");}
        }
        throw new BadRequestException("idk");
    }

    private static GameData findGame(int gameID, GameDAO gameDAO) throws DataAccessException{
        for (GameData game : gameDAO.findGames()){
            if (game.gameID() == gameID){
                return game;
            }
        }
        return null;
    }
}
