package dataaccess;

import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO{
    private final HashSet<GameData> gameDatabase = new HashSet<>();

    public void clear() {
        gameDatabase.clear();
    }
    public HashSet<GameData> findGames(){
        return gameDatabase;
    }
}
