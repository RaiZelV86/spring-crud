package kr.crud.crudproject.config;

import kr.crud.crudproject.person.model.Person;
import kr.crud.crudproject.person.model.Role;
import kr.crud.crudproject.person.repository.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInit {

    @Bean
    public CommandLineRunner initAdmin(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@gmail.com";

            if (personRepository.existsByEmail(adminEmail)) {
                System.out.println("✅ Admin already exists: " + adminEmail);
                return;
            }

            Person admin = new Person();
            admin.setFirstName("Admin");
            admin.setLastName("Super");
            admin.setAge(66);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(Role.ADMIN);

            personRepository.save(admin);
            System.out.println("🛠️ Admin created: " + adminEmail);
        };
    }
}
