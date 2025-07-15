package service;

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
}
