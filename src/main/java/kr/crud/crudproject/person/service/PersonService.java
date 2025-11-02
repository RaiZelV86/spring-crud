package kr.crud.crudproject.person.service;

import kr.crud.crudproject.person.dto.PersonRequest;
import kr.crud.crudproject.person.dto.PersonResponse;
import kr.crud.crudproject.person.model.Person;
import kr.crud.crudproject.person.model.Role;
import kr.crud.crudproject.person.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public PersonResponse createPerson(PersonRequest personRequest) {
        if (personRepository.existsByEmail(personRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists!");
        }

        Person person = new Person();
        person.setEmail(personRequest.getEmail());
        person.setPassword(passwordEncoder.encode(personRequest.getPassword()));
        person.setFirstName(personRequest.getFirstName());
        person.setLastName(personRequest.getLastName());
        person.setAge(personRequest.getAge());
        person.setRole(Role.USER);

        Person saved = personRepository.save(person);

        return new PersonResponse(
                saved.getId(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getAge(),
                saved.getEmail(),
                saved.getRole().name()

        );
    }

    public List<PersonResponse> getAllPersons() {
        return personRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PersonResponse getPersonById(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Person not found with id: " + id));
        return toResponse(person);
    }

    private PersonResponse toResponse(Person person) {
        return new PersonResponse(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getAge(),
                person.getEmail(),
                person.getRole().name()
        );
    }

    public PersonResponse updatePerson(Long id, PersonRequest request) {

        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Person not found with id: " + id));

        if (!request.getEmail().equals(person.getEmail()) &&
                personRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists!");
        }

        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setAge(request.getAge());

        if (!request.getEmail().equals(person.getEmail())
                && personRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new IllegalArgumentException("Email already exists!");
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            person.setPassword(passwordEncoder.encode(request.getPassword()));
        }


        Person updated = personRepository.save(person);

        return new PersonResponse(
                updated.getId(),
                updated.getFirstName(),
                updated.getLastName(),
                updated.getAge(),
                updated.getEmail(),
                updated.getRole().name()
        );
    }

    public void deletePerson(Long id) {
        if (!personRepository.existsById(id)) {
            throw new IllegalArgumentException("Cannot delete: person with id " + id + " not found");
        }
        personRepository.deleteById(id);
    }

}
