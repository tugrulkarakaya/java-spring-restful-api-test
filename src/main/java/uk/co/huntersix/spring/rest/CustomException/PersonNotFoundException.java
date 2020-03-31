package uk.co.huntersix.spring.rest.CustomException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code =  HttpStatus.NO_CONTENT, reason = "There is no person in the records!")
public class PersonNotFoundException extends RuntimeException {
    public PersonNotFoundException() {
        super("There is no person in the records!");
    }
}
