package vb.shop.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class PasswordsDoNotMatchException extends RuntimeException{
        public PasswordsDoNotMatchException(){
            super("Passwords doesn't match!!");
        }
}
