package kr.crud.crudproject.person.controller;

import jakarta.validation.Valid;
import kr.crud.crudproject.person.dto.PersonRequest;
import kr.crud.crudproject.person.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String register(@Valid @ModelAttribute("person") PersonRequest personRequest,
                           BindingResult bindingResult,
                           Model model) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            personService.createPerson(personRequest);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "register";
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home() {
        return "homePage";
    }
}
