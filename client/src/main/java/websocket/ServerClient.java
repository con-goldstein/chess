package websocket;

import websocket.messages.ServerMessage;

public class ServerClient implements ServerMessageObserver{
    @Override
    public void notify(ServerMessage message){
        switch (message.getServerMessageType()){
            default -> System.out.println("Temp line");
        }
    }
}
