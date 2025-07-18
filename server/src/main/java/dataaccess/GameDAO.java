package dataaccess;

import results.CreateResult;
import model.*;

import java.util.HashSet;

public interface GameDAO {
    void clear();
    HashSet<GameData> findGames();
    CreateResult createGameData(String gamename, String authToken) throws BadRequestException;
    void addGame(GameData game);
}
