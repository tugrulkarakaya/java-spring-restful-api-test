package uk.co.huntersix.spring.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.co.huntersix.spring.rest.CustomException.PersonExistsException;
import uk.co.huntersix.spring.rest.CustomException.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void shouldReturnNotFoundResponseIfPersonNonExists() throws Exception {
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



    @Test
    @DisplayName("Should add person to db")
    public void shouldInsertPerson() throws Exception {
        given(personDataService.insertPerson(any(),any())).willReturn(new Person("Mike", "Frijson"));

        MvcResult mvcResult =  this.mockMvc.perform(post("/person")
                .param("firtName","Mike")
                .param("lastName","Frijson"))
                .andExpect(status().isCreated())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    public void shouldReturnEmptyResultForNoPeopleSearch() throws Exception {
        given(personDataService.findAll(any())).willReturn(Collections.emptyList());

        this.mockMvc.perform(get("/person/adam"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$",hasSize(0)));
    }
    @Test
    public void shouldReturnConflictWhenPersonAlreadyExist() throws Exception {
        Map<String, String> elements = new HashMap();
        elements.put("firstName", "Tugrul");
        elements.put("lastName", "Karakaya");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(elements);

        given(personDataService.insertPerson(any(),any())).willThrow(PersonExistsException.class);

        this.mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(status().reason("Person already exists!"));

    }


}