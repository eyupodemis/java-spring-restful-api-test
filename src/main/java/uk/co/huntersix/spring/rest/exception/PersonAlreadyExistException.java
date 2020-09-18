package uk.co.huntersix.spring.rest.exception;

public class PersonAlreadyExistException extends RuntimeException{

    public PersonAlreadyExistException(String firstName, String lastName){
        super("Person :" + firstName + " " + lastName + " already exist." );
    }
}
