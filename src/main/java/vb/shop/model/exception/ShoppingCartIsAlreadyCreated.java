package vb.shop.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
public class ShoppingCartIsAlreadyCreated extends RuntimeException {
  public ShoppingCartIsAlreadyCreated(){
      super("Shopping cart already created");
  }
}
