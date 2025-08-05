package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand {
    public final ChessMove move;
    public MakeMoveCommand(ChessMove move){
        this.move = move;
    }

    public ChessMove getMove(){
        return move;
    }
}
