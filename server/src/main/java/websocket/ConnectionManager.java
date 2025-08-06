package websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();

public void saveSession(int gameID, Connection connection){
    if (!connections.containsKey(gameID)){
        ArrayList<Connection> lst = new ArrayList<>();
        lst.add(connection);
        connections.put(gameID,lst);
    }
    else{
        if (!connections.get(gameID).contains(connection)){
            connections.get(gameID).add(connection);
        }
    }
}

public void add(int gameID, Connection connection){
    connections.get(gameID).add(connection);
}

public void broadcastNotToUser(String message, int gameID, String username) throws IOException {
    var removeList = new ArrayList<Connection>();
    var connectionList = connections.get(gameID);
        //for Connection in array list
        for (Connection c : connectionList) {
            if(c.session.isOpen()){
                String cUsername = c.getUsername();
                //if username != rootuser
                if (!cUsername.equals(username)) {
                    //send notification to all other users
                    c.send(message.toString());
                }
            }
            else {
                //Connection session is closed, remove connection from connections
                removeList.add(c);
            }
        }
    connectionList.removeAll(removeList);
}

public void broadcastToUser(String message, int gameID, String username) throws IOException {
        var connectionList = connections.get(gameID);
        for (Connection c : connectionList){
            if (c.session.isOpen()){
                String cUsername = c.getUsername();
                if (cUsername.equals(username)){
                    c.send(message);
                }
            }
            else {
                connectionList.remove(c);
            }
        }
}

public void broadcastToAll(String message, int gameID) throws IOException {
    var connectionList = connections.get(gameID);
    for (Connection c : connectionList){
        if (c.session.isOpen()){
                c.send(message);
        }
        else {
            connectionList.remove(c);
        }
    }
}

public void remove(int gameID, String username, Session session){
    connections.get(gameID).remove(new Connection(username, session));
}
}