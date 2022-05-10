package vb.shop.model.exception;

public class InvalidUserCredentials extends RuntimeException{
    public InvalidUserCredentials(){
        super("Invalid Credentials");
    }
}
