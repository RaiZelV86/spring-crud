package kr.crud.crudproject.person.controller.rest;

import jakarta.validation.Valid;
import kr.crud.crudproject.person.dto.PersonRequest;
import kr.crud.crudproject.person.dto.PersonResponse;
import kr.crud.crudproject.person.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<PersonResponse> createPerson(@RequestBody PersonRequest personRequest) {
        PersonResponse personResponse = personService.createPerson(personRequest);
        return ResponseEntity.ok(personResponse);
    }

    @GetMapping
    public ResponseEntity<List<PersonResponse>> getAllPersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> getPersonById(@PathVariable Long id) {
        return ResponseEntity.ok(personService.getPersonById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponse> updatePerson(@PathVariable Long id,
                                                       @Valid @RequestBody PersonRequest personRequest) {
       return ResponseEntity.ok(personService.updatePerson(id, personRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PersonResponse> deletePersonById(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/persons")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public List<PersonResponse> getAll() {
        return personService.getAllPersons();
    }
}
