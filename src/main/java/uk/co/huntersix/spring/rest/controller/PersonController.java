package uk.co.huntersix.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.List;

@RestController
public class PersonController {
    private PersonDataService personDataService;

    public PersonController(@Autowired PersonDataService personDataService) {
        this.personDataService = personDataService;
    }

    @GetMapping("/person/{lastName}/{firstName}")
    public Person person(@PathVariable(value="lastName") String lastName,
                         @PathVariable(value="firstName") String firstName) {
        return personDataService.findPerson(lastName, firstName);
    }

    @GetMapping("/person/{filter}")
    public ResponseEntity<List<Person>> person(@PathVariable(value="filter") String filter) {
        return new ResponseEntity<>(personDataService.findAll(filter), HttpStatus.OK);
    }

    @PostMapping("/person")
    public ResponseEntity<Person> person(Person person) {
        return new ResponseEntity<>(personDataService.insertPerson(person.getLastName(),person.getFirstName()), HttpStatus.OK);
    }
}