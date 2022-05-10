package vb.shop.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AuthorDoesNotExistException extends RuntimeException {
    public AuthorDoesNotExistException(){
        super("Author with the given id wasn't found");
    }
}
