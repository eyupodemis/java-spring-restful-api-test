package uk.co.huntersix.spring.rest.exception;

public class PersonNotFoundException extends RuntimeException{

    public PersonNotFoundException(String firstName, String lastName){
        super("Person :" + firstName + " " + lastName + " does not exist." );
    }
}
