package uk.co.huntersix.spring.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {


    @ExceptionHandler(PersonNotFoundException.class)
    public final ResponseEntity<Map<String, Object>> handlePersonNotFoundException(PersonNotFoundException ex) {
        return new ResponseEntity("Person cannot be found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PersonAlreadyExistException.class)
    public final ResponseEntity<Map<String, Object>> handlePersonAlreadyCreatedException(PersonAlreadyExistException ex) {
        return new ResponseEntity("Person already exist", HttpStatus.UNPROCESSABLE_ENTITY);
    }


    @ExceptionHandler(Exception.class)
    public final ResponseEntity handleAllExceptions(Exception ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
