package uk.co.huntersix.spring.rest.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.huntersix.spring.rest.model.Person;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class HttpRequestTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void shouldReturnPersonDetails() throws Exception {
        assertThat(
            this.restTemplate.getForObject(
                "http://localhost:" + port + "/person/smith/mary",
                String.class
            )
        ).contains("Mary");
    }

    @Test
    public void shouldReturnNotFoundResponse() {
       assertThat(
                this.restTemplate.getForEntity(
                        "http://localhost:" + port + "/person/non-existing-lastname/non-existing-firstname",
                        String.class
                ).getStatusCode()
       ).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void callBySurnameShouldReturnSingleRecord() throws Exception {
        Person[] personArray = this.restTemplate.getForObject(
                "http://localhost:" + port + "/person/Archer",
                Person[].class
        );
        assertThat(personArray[0].getFirstName()).isEqualTo("Brian");
        assertThat(personArray.length).isEqualTo(1);
    }

    @Test
    public void callBySurnameShouldReturnMultipleRecord() throws Exception {
        this.restTemplate.postForEntity(
                "http://localhost:" + port + "/person/Archer/non-existing-firstname",
                String.class, null);

        // Add one more Archer surname to the repository
        Person[] personArray = this.restTemplate.getForObject(
                "http://localhost:" + port + "/person/Archer",
                Person[].class
        );
        assertThat(personArray.length).isEqualTo(2);
    }

    @Test
    public void callBySurnameShouldReturnEmptyWhenNonExistingSurname() {
        Person[] personArray = this.restTemplate.getForObject(
                "http://localhost:" + port + "/person/non-existing-lastname",
                Person[].class);
        assertThat(personArray.length).isEqualTo(0);
    }

    @Test
    public void postRequestShouldAddRecord() throws Exception {
        assertThat(
                this.restTemplate.getForEntity(
                        "http://localhost:" + port + "/person/non-existing-lastname/non-existing-firstname",
                        String.class
                ).getStatusCode()
        ).isEqualTo(HttpStatus.NOT_FOUND);

        this.restTemplate.postForEntity(
                "http://localhost:" + port + "/person/non-existing-lastname/non-existing-firstname",
                String.class, null);

        assertThat(
                this.restTemplate.getForObject(
                        "http://localhost:" + port + "/person/non-existing-lastname/non-existing-firstname",
                        String.class
                )
        ).contains("non-existing-firstname");
    }


    @Test
    public void postRequestShouldReturnUnprocessableEntityWhenAlreadyExist() throws Exception {
        Person person = this.restTemplate.getForObject(
                "http://localhost:" + port + "/person/Archer/Brian",
                Person.class
        );

        assertThat(person.getFirstName()).isEqualTo("Brian");

        assertThat(
                this.restTemplate.postForEntity(
                        "http://localhost:" + port + "/person/Archer/Brian",
                        null, null
                ).getStatusCode()
        ).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

}