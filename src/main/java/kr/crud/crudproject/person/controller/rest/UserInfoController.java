package kr.crud.crudproject.person.controller.rest;

import jakarta.validation.Valid;
import kr.crud.crudproject.person.dto.PersonRequest;
import kr.crud.crudproject.person.dto.PersonResponse;
import kr.crud.crudproject.person.model.Person;
import kr.crud.crudproject.person.repository.PersonRepository;
import kr.crud.crudproject.config.PersonDetails;
import kr.crud.crudproject.person.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoController {

    private final PersonRepository personRepository;
    private final PersonService personService;

    @Autowired
    public UserInfoController(PersonRepository personRepository, PersonService personService) {
        this.personRepository = personRepository;
        this.personService = personService;
    }

    @GetMapping("/api/me")
    public PersonResponse getCurrentUser(Authentication authentication) {
        Person person = personRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new PersonResponse(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getAge(),
                person.getEmail(),
                person.getRole().name()
        );
    }

    @PutMapping("/api/me")
    public ResponseEntity<?> updateCurrentUser(@Valid @RequestBody PersonRequest request,
                                               BindingResult bindingResult,
                                               Authentication auth) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        return ResponseEntity.ok(personService.updatePersonByEmail(auth.getName(), request));
    }
}
