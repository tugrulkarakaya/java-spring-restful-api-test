package uk.co.huntersix.spring.rest.referencedata;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.co.huntersix.spring.rest.CustomException.PersonExistsException;
import uk.co.huntersix.spring.rest.CustomException.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("Person Data Service Test")
class PersonDataServiceTest {

    private PersonDataService service = new PersonDataService();

    @DisplayName("Test if can find a person")
    @Test
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