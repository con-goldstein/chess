package websocket;

import websocket.messages.ServerMessage;

public class ChessClient implements ServerMessageObserver{
    @Override
    public void notify(String message){
        switch (message){
//            case NOTIFICATION -> displayNotification((NotificationMessage) message).getMessage();
            default -> System.out.println("Temp line");
        }
    }
}
