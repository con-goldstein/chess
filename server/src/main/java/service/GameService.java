package service;

import Requests.*;
import Results.CreateResult;
import dataaccess.*;
import model.GameData;
import java.util.HashSet;

public class GameService {
//    public CreateResult create(CreateRrequest createRequest){}
//    public JoinResult join(JoinRequest joinRequest){}
    public static HashSet<GameData> list(String authToken, AuthDAO authDAO, GameDAO gameDAO) throws UnauthorizedException{
        boolean foundToken = authDAO.findAuthToken(authToken);
        if (!foundToken){
            throw new UnauthorizedException("could not find authToken");
        }
        return gameDAO.findGames();
    }

    public static CreateResult create(CreateRequest createRequest, AuthDAO authDAO, GameDAO gameDAO)
    throws UnauthorizedException, BadRequestException{
        boolean foundToken = authDAO.findAuthToken(createRequest.authToken());
        if (!foundToken){
            throw new UnauthorizedException("could not find authToken");
        }
        if ((createRequest.authToken() == null) || (createRequest.gamename() == null)){
            throw new BadRequestException("authToken or gamename required");
        }
        return gameDAO.createGameData(createRequest.gamename(), createRequest.authToken());
    }
}
