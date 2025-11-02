package kr.crud.crudproject.person.controller;

import jakarta.validation.Valid;
import kr.crud.crudproject.person.dto.PersonRequest;
import kr.crud.crudproject.person.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private PersonService personService;

    @Autowired
    public AuthController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("person", new PersonRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("person") PersonRequest personRequest) {
        personService.createPerson(personRequest);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
