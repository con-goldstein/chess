package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private final HashMap<String, UserData> userDatabase = new HashMap<>();

    public void clear() {
        userDatabase.clear();
    }

}
