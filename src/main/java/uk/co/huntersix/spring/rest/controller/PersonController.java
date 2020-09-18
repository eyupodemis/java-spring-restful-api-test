package uk.co.huntersix.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.List;

@RestController
public class PersonController {
    private PersonDataService personDataService;

    public PersonController(@Autowired PersonDataService personDataService) {
        this.personDataService = personDataService;
    }

    @GetMapping("/person/{lastName}/{firstName}")
    public Person getPerson(@PathVariable(value="lastName") String lastName,
                         @PathVariable(value="firstName") String firstName) {
        return personDataService.findPerson(lastName, firstName);
    }

    @GetMapping("/person/{lastName}")
    public List<Person> getPerson(@PathVariable(value="lastName") String lastName) {
        return personDataService.findPeopleBySurname(lastName);
    }

    @PostMapping("/person/{lastName}/{firstName}")
    public ResponseEntity<Person> addPerson(@PathVariable(value="lastName") String lastName,
                                            @PathVariable(value="firstName") String firstName) {
        return new ResponseEntity<Person>(personDataService.addPerson(lastName, firstName),
                HttpStatus.CREATED);
    }
}