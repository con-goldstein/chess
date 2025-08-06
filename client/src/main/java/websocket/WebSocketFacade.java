//package WebSocket;
//import com.sun.nio.sctp.NotificationHandler;
//import websocket.messages.ServerMessage;
//
//import javax.imageio.spi.ServiceRegistry;
//import javax.websocket.*;
//
//public class WebSocketFacade extends Endpoint {
//    Session session;
//    ServerMessageObserver observer;
//
//    public WebSocketFacade(String url, ServerMessageObserver observer) throws Exception {
//        this.observer = observer;
//    }
//    @Override
//    public void onOpen(Session session, EndpointConfig endpointConfig) {
//    }
//
//    public void onMessage(String message) {
//        try {
//            ServerMessage msg = gson.fromJson(message, ServerMessage.class);
//            observer.notify(msg);
//        } catch (Exception ex){
////            observer.notify(new ServerMessage("Error Error Error"));
//        }
//    }
//}
