package kr.crud.crudproject.person.controller;

import kr.crud.crudproject.person.model.Person;
import kr.crud.crudproject.person.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PersonRepository personRepository;

    @Autowired
    public AdminController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        String email = authentication.getName();

        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        model.addAttribute("admin", person);

        return "admin/profile";
    }
}
