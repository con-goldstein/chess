package dataaccess;

public class UnauthorizedException extends Exception{
    public UnauthorizedException(String message){
        super(message);
    }
}
