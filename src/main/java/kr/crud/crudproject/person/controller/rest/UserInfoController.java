package kr.crud.crudproject.person.controller.rest;

import kr.crud.crudproject.person.model.Person;
import kr.crud.crudproject.person.repository.PersonRepository;
import kr.crud.crudproject.person.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoController {

    private final PersonRepository personRepository;

    @Autowired
    public UserInfoController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping("/api/me")
    public Person getCurrentUser(@AuthenticationPrincipal PersonDetails personDetails) {
        if (personDetails == null) {
            throw new RuntimeException("No authenticated user found");
        }

        Person person = personDetails.getPerson();
        if (person == null) {
            throw new RuntimeException("Person object is null for current user");
        }
        return person;
    }
}
