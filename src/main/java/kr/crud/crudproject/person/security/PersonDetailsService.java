package kr.crud.crudproject.person.security;

import kr.crud.crudproject.person.model.Person;
import kr.crud.crudproject.person.repository.PersonRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PersonDetailsService implements UserDetailsService {

    private PersonRepository personRepository;

    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person person = personRepository.findByEmail(email).
                orElseThrow (() -> new UsernameNotFoundException("User not found with email: " + email));
        return new PersonDetails(person);
    }
}
