package uk.co.huntersix.spring.rest.referencedata;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.co.huntersix.spring.rest.CustomException.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.Assertions.catchThrowable;

class PersonDataServiceTest {

    private PersonDataService service = new PersonDataService();

    @DisplayName("Test if find person")
    @Test
    void findPerson() {
    }

    @Test
    @DisplayName("Test person not found exception")
    void PersonNotFoundExceptionHandler() {
        //given
        final String firstName="Tugrul";
        final String lastName="Karakaya";

        //when
        Throwable throwable =  catchThrowable(()->service.findPerson(lastName,firstName));

        //then
        then(throwable).as("PNF should be thrown if the person with the name and lastname is not found")
                .isInstanceOf(PersonNotFoundException.class)
                .as("Check that message exception contains")
                .hasMessage("There is no person in the records!");

    }

}