package uk.co.huntersix.spring.rest.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
        List foundHashMap = restTemplate.getForObject("/person/"+person.getFirstName(), List.class);
        ObjectMapper mapper = new ObjectMapper();
        List<Person> found = mapper.convertValue(foundHashMap, new TypeReference<List<Person>>() { });

        assertThat(found).hasSize(1);
        assertThat(found.get(0)).as("Fetched person should match")
                .isEqualTo(person);

    }
}
