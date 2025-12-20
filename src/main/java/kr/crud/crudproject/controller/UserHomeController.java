package kr.crud.crudproject.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserHomeController {

    @GetMapping("/home")
    public String userHome(Authentication authentication, Model model) {
        String email = authentication.getName();
        model.addAttribute("email", email);
        return "user/home";
    }
}

