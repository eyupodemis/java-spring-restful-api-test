package uk.co.huntersix.spring.rest.referencedata;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.huntersix.spring.rest.exception.PersonAlreadyExistException;
import uk.co.huntersix.spring.rest.exception.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonDataServiceTest {

    @Autowired
    PersonDataService personDataService;

    @Test
    public void shouldFindPerson() {
        Person person = personDataService.findPerson("Smith", "Mary");
        assertThat(person.getFirstName()).isEqualTo("Mary");
        assertThat(person.getLastName()).isEqualTo("Smith");
    }

    @Test
    public void shouldFindPeopleBySurname() {
        List<Person> person = personDataService.findPeopleBySurname("Smith");
        assertThat(person.get(0).getFirstName()).isEqualTo("Mary");
        assertThat(person.get(0).getLastName()).isEqualTo("Smith");
    }

    @Test
    public void shouldAddPersonToRepository() {
        personDataService.addPerson("non-existing-surname", "non-existing-name");
        assertThat(personDataService.findPerson("non-existing-surname", "non-existing-name")
                .getFirstName()).isEqualTo("non-existing-name");
    }

    @Test(expected = PersonAlreadyExistException.class)
    public void shouldReturnExceptionWhenExistingRecordAdded() {
        personDataService.addPerson("Smith", "Mary");
    }

    @Test(expected = PersonNotFoundException.class)
    public void shouldReturnExceptionWhenDataNotFoundByFullName() {
        personDataService.findPerson("non-existing-surname", "non-existing-name");
    }
}
