package WebSocket;

import websocket.messages.ServerMessage;

public class ServerClient implements ServerMessageObserver{
    @Override
    public void notify(ServerMessage message){
        switch (message.getServerMessageType()){
//            case NOTIFICATION -> displayNotification(((NotificationMessage) message).getMessage());
//            case ERROR -> displayError(((ErrorMessage) message).getErrorMessage());
//            case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
            default -> System.out.println("Temp line");
        }
    }
}
