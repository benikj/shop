package vb.shop.model.exception;


public class ShoppingCartIsNotActiveException extends RuntimeException {
    public ShoppingCartIsNotActiveException(String userId){
        super(String.format("There is no active shopping cart for user %s",userId));
    }
}
