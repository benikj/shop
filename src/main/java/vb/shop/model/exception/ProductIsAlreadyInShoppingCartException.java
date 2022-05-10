package vb.shop.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED)
public class ProductIsAlreadyInShoppingCartException extends RuntimeException{
    public ProductIsAlreadyInShoppingCartException(){
        super("This product is already in shopping cart");
    }
}
