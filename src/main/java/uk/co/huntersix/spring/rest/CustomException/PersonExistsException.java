package uk.co.huntersix.spring.rest.CustomException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Person already exists!")
public class PersonExistsException extends RuntimeException {
    public PersonExistsException() {
        super("Person already exists!");
    }
}
