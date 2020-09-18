package uk.co.huntersix.spring.rest.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.co.huntersix.spring.rest.exception.PersonAlreadyExistException;
import uk.co.huntersix.spring.rest.exception.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonDataService personDataService;

    @Test
    public void shouldReturnPersonFromService() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(new Person("Mary", "Smith"));
        this.mockMvc.perform(get("/person/smith/mary"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("firstName").value("Mary"))
            .andExpect(jsonPath("lastName").value("Smith"));
    }

    @Test
    public void shouldReturnNotFoundResponse() throws Exception {
        when(personDataService.findPerson(any(), any())).thenThrow(PersonNotFoundException.class);
        this.mockMvc.perform(get("/person/non-existing-lastname/non-existing-firstname"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void callBySurnameShouldReturnEmptyBody() throws Exception {
        when(personDataService.findPeopleBySurname(any())).thenReturn(Collections.emptyList());
        this.mockMvc.perform(get("/person/non-existing-lastname"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void callBySurnameShouldReturnMultiple() throws Exception {
        final List<Person> personList = Arrays.asList(
                new Person("Mary", "Smith"),
                new Person("Brian", "Smith"),
                new Person("Collin", "Smith")
        );

        when(personDataService.findPeopleBySurname(any())).thenReturn(personList);
        this.mockMvc.perform(get("/person/Smith"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].firstName").value("Mary"))
                .andExpect(jsonPath("$[1].firstName").value("Brian"));
    }

    @Test
    public void shouldReturnCreatedStatusWhenNewRecordAdded() throws Exception {
        when(personDataService.addPerson(any(), any())).thenReturn(new Person("newPersonSurname", "newPersonName"));
        this.mockMvc.perform(post("/person/newPersonSurname/newPersonName"))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnUnprocessableEntityWhenExistingRecordAdded() throws Exception {
        when(personDataService.addPerson(any(), any())).thenThrow(new PersonAlreadyExistException("Mary","Smith"));
        MvcResult result = this.mockMvc.perform(post("/person/Smith/Mary"))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }
}