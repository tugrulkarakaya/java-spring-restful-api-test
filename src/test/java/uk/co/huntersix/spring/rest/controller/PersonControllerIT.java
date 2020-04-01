package uk.co.huntersix.spring.rest.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonControllerIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Find People Integration Test")
    void testFindPeopleIntegration() {
        List<Person> found =(List<Person>) restTemplate.getForObject("/person/mary", List.class);
        assertThat(found).hasSize(1);
    }
}
