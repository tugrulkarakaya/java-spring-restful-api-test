package uk.co.huntersix.spring.rest.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.co.huntersix.spring.rest.CustomException.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
@DisplayName("Person Controller Test")
public class PersonControllerTest {

    Person validPerson;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PersonDataService personDataService;

    @BeforeEach
    void setup() {
        validPerson = new Person("Mary", "Smith");
    }

    @Test
    @DisplayName("Should Return Person From Service")
    public void shouldReturnPersonFromService() throws Exception {
        given(personDataService.findPerson(any(), any())).willReturn(validPerson);

        MvcResult result = this.mockMvc.perform(get("/person/smith/mary"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("firstName").value("Mary"))
                .andExpect(jsonPath("lastName").value("Smith"))
                .andReturn();

        System.out.println(result);
    }

    @Test
    @DisplayName("Should Return Person From Service")
    public void shouldReturn204ResponseIfPersonNonExists() throws Exception {
        given(personDataService.findPerson(any(), any())).willThrow(PersonNotFoundException.class);

        this.mockMvc.perform(get("/person/smith/mary"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should Return People List From Service")
    public void shouldFindPeopleList() throws Exception {
        given(personDataService.findAll(any())).willReturn(Arrays.asList(validPerson, new Person("Tugrul", "Smith")));

        MvcResult mvcResult =  this.mockMvc.perform(get("/person/smith").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName",is(validPerson.getFirstName())))
                .andExpect(jsonPath("$[1].firstName",is("Tugrul")))
                .andReturn();
    }

}