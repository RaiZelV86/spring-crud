package kr.crud.crudproject.config;

import kr.crud.crudproject.model.User;
import kr.crud.crudproject.model.Role;
import kr.crud.crudproject.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInit {

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@gmail.com";

            if (userRepository.existsByEmail(adminEmail)) {
                System.out.println("✅ Admin already exists: " + adminEmail);
                return;
            }

            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("Super");
            admin.setAge(66);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
            System.out.println("🛠️ Admin created: " + adminEmail);
        };
    }
}
