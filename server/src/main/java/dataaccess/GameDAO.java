package dataaccess;

import results.CreateResult;
import model.*;

import java.util.HashSet;

public interface GameDAO {
    void clear();
    HashSet<GameData> findGames() throws DataAccessException;
    CreateResult createGameData(String gamename) throws BadRequestException;
    void addGame(GameData game) throws DataAccessException;
}
