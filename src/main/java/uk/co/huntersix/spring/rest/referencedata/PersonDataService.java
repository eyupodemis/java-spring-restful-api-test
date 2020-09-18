package uk.co.huntersix.spring.rest.referencedata;

import org.springframework.stereotype.Service;
import uk.co.huntersix.spring.rest.exception.PersonAlreadyExistException;
import uk.co.huntersix.spring.rest.exception.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PersonDataService {
    private Set<Person> personDataList = new HashSet<>(Arrays.asList(
            new Person("Mary", "Smith"),
            new Person("Brian", "Archer"),
            new Person("Collin", "Brown")
    ));

    public Person findPerson(String lastName, String firstName) {
         return getPerson(lastName, firstName)
                .orElseThrow(() -> new PersonNotFoundException(firstName,lastName));
    }

    public List<Person> findPeopleBySurname(String lastName) {
        return personDataList.stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    public Person addPerson(String lastName, String firstName) {
        if(getPerson(lastName, firstName).isPresent()) {
            throw new PersonAlreadyExistException(firstName, lastName);
        }

        personDataList.add(new Person(firstName, lastName));
        return getPerson(lastName, firstName).get();
    }

    private Optional<Person> getPerson(String lastName, String firstName) {
        return personDataList.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }
}
