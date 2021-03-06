package uk.co.huntersix.spring.rest.referencedata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import uk.co.huntersix.spring.rest.CustomException.PersonExistsException;
import uk.co.huntersix.spring.rest.CustomException.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("Person Data Service Test")
class PersonDataServiceTest {

    private PersonDataService service;

    @BeforeEach
    void setUp() {
        service  = new PersonDataService();
    }

    @DisplayName("Test if can get a person")
    @Test
    @Order(100) //it is good to execute this after testing get person
    void insertPersonShouldAddNewRecord() {
        //given
        final String firstName="Meriam";
        final String lastName="Blacsmith";

        //when
        Person actualPerson = service.insertPerson(lastName,firstName);
        Person found = service.findPerson(lastName,firstName);
        //then
        then(actualPerson).as("the person should be saved")
                .isEqualTo(found);

    }

    @DisplayName("Test if can get a person")
    @Test
    @Order(1)
    void findPersonShouldGetPerson() {
        //given
        final String firstName="Mary";
        final String lastName="Smith";

        //when
        Person actualPerson = service.findPerson(lastName,firstName);

        //then
        then(firstName).as("Check that expected person firstName is equal to actual")
                .isEqualTo(actualPerson.getFirstName());

        then(lastName).as("Check that expected person lastName is equal to actual")
                .isEqualTo(actualPerson.getLastName());
    }

    @DisplayName("Test if can find all people")
    @ParameterizedTest(name = "{displayName} - [{index}] {arguments}")
    @CsvSource({
            "Karakaya, 2",
            "Brown, 4",
            "Golan, 1",
    })
    void findAllShouldGetAllRecords(String filter, int foundResult) {
        //given
        service.insertPerson("Golan","Brian");
        service.insertPerson("Karakaya","Hasan");
        service.insertPerson("Brown","Meryem");
        service.insertPerson("Karakaya","Tugrul");
        service.insertPerson("Brown","Mary");
        service.insertPerson("Brown","Elly");


        //when
        List<Person> people = service.findAll(filter);

        //then
        then(people).as(filter + " should have been found "+foundResult+" times in the records")
                .hasSize(foundResult);
    }

    @Test
    @DisplayName("Test if find all thrown exception when ther eis no record found")
    void findAllShouldThrowExceptionIfRecordNotFound() {
        //given
        final String filter="non-exists-term";

        //when
        Throwable throwable =  catchThrowable(()->service.findAll(filter));

        //then
        then(throwable).as("Person Not Found should be thrown if the filter does not match any name or lastname")
                .isInstanceOf(PersonNotFoundException.class)
                .as("Person Not Found exception message contains")
                .hasMessage("There is no person in the records!");

    }

    @Test
    @DisplayName("Test PersonNotFoundException and exception message")
    void PersonNotFoundExceptionHandler() {
        //given
        final String firstName="Tugrul";
        final String lastName="Karakaya";

        //when
        Throwable throwable =  catchThrowable(()->service.findPerson(lastName,firstName));

        //then
        then(throwable).as("Person Not Found should be thrown if the person with the name and lastname is not found")
                .isInstanceOf(PersonNotFoundException.class)
                .as("Check that message exception contains")
                .hasMessage("There is no person in the records!");

    }

    @Test
    @DisplayName("Same Person should not be added")
    void PersonAlreadyExistsExceptionHandler() {
        //given
        final String firstName="Mary";
        final String lastName="Smith";

        //when
        Throwable throwable =  catchThrowable(()->service.insertPerson(lastName,firstName));

        //then
        then(throwable).as("Person Exists Exception should be thrown if the person with the name and lastname already exists")
                .isInstanceOf(PersonExistsException.class)
                .as("Check that message exception contains")
                .hasMessage("Person already exists!");

    }

}