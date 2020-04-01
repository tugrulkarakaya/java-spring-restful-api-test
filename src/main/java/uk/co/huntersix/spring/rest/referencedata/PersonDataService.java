package uk.co.huntersix.spring.rest.referencedata;

import org.springframework.stereotype.Service;
import uk.co.huntersix.spring.rest.CustomException.PersonExistsException;
import uk.co.huntersix.spring.rest.CustomException.PersonNotFoundException;

import uk.co.huntersix.spring.rest.model.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Service
public class PersonDataService {
    public static final List<Person> PERSON_DATA = Arrays.asList(
        new Person("Mary", "Smith"),
        new Person("Brian", "Archer"),
        new Person("Collin", "Brown")
    );

    private final List<Person> repository = new ArrayList<>(PERSON_DATA);

    BiPredicate<List<Person>, Person> listHasPersonBiPredicate = (list, person) ->
        list.stream().anyMatch(p->p.getLastName().equalsIgnoreCase(person.getLastName())
                                 && p.getFirstName().equalsIgnoreCase(person.getFirstName()));

    public Person findPerson(String lastName, String firstName) {
        return  repository.stream()
            .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst().orElseThrow(PersonNotFoundException::new);
    }

    public Person insertPerson(String lastName, String firstName) {
        Person newPerson = new Person(firstName,lastName);
        if(listHasPersonBiPredicate.test(repository, newPerson))
        {
            throw new PersonExistsException();
        }
        repository.add(newPerson);
        return newPerson;
    }

    public List<Person> findAll(String filter) {
        List<Person> people=  repository.stream().filter(p-> p.getLastName().equalsIgnoreCase(filter))
                .collect(Collectors.toList());
        if(0== people.size()) {
            throw new PersonNotFoundException();
        }

        return people;
    }

    public Person getFirst() {
        //ToDo: needs more  unit test. I created this method just for Integration test for demo purposes
        return repository.get(0);
    }
}
