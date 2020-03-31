package uk.co.huntersix.spring.rest.referencedata;

import org.springframework.stereotype.Service;
import uk.co.huntersix.spring.rest.CustomException.PersonExistsException;
import uk.co.huntersix.spring.rest.CustomException.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;

@Service
public class PersonDataService {
    public static final List<Person> PERSON_DATA = Arrays.asList(
        new Person("Mary", "Smith"),
        new Person("Brian", "Archer"),
        new Person("Collin", "Brown")
    );

    BiPredicate<List<Person>, Person> listHasPersonBiPredicate = (list, person) ->
        list.stream().anyMatch(p->p.getLastName().equalsIgnoreCase(person.getLastName())
                                 && p.getFirstName().equalsIgnoreCase(person.getFirstName()));

    public Person findPerson(String lastName, String firstName) {
        return  PERSON_DATA.stream()
            .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst().orElseThrow(PersonNotFoundException::new);
    }

    public Person insertPerson(String lastName, String firstName) {
        Person newPerson = new Person(firstName,lastName);
        if(listHasPersonBiPredicate.test(PERSON_DATA, newPerson))
        {
            throw new PersonExistsException();
        }
        PERSON_DATA.add(newPerson);
        return newPerson;
    }
}
