package uk.co.huntersix.spring.rest.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.Collections;
import java.util.List;
import java.util.Map;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Person Controller Integration Test")
public class PersonControllerIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    PersonDataService personDataService;

    Person person;

    @BeforeEach
    void setUp(){
        person = personDataService.getFirst();
    }

    @Test
    @DisplayName("Find People Integration Test")
    void testFindPeopleIntegration() {
        List foundHashMap = restTemplate.getForObject("/person/"+person.getLastName(), List.class);
        ObjectMapper mapper = new ObjectMapper();
        List<Person> found = mapper.convertValue(foundHashMap, new TypeReference<List<Person>>() { });

        assertThat(found).hasSize(1);
        assertThat(found.get(0)).as("Fetched person should match")
                .isEqualTo(person);

    }

    @Test
    @DisplayName("Insert person Integration Test")
    void shouldInsertPerson() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("firstName","Maria");
        map.add("lastName","Blackstone");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);;
        ResponseEntity response = restTemplate.postForEntity("/person",request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void shouldReturnConflictWhenPersonAlreadyExist()  {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("firstName","Mary");
        map.add("lastName","Smith");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);;

        ResponseEntity<Map> response = restTemplate.postForEntity("/person",request, Map.class);

        assertThat(HttpStatus.CONFLICT).isEqualTo(response.getStatusCode());
        assertThat ("Person already exists!").isEqualTo(response.getBody().get("message"));
    }

}
