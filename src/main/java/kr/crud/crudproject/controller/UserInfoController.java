package kr.crud.crudproject.controller;

import jakarta.validation.Valid;
import kr.crud.crudproject.dto.UserRequest;
import kr.crud.crudproject.dto.UserResponse;
import kr.crud.crudproject.model.User;
import kr.crud.crudproject.repository.UserRepository;
import kr.crud.crudproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoController {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserInfoController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/api/me")
    public UserResponse getCurrentUser(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getAge(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    @PutMapping("/api/me")
    public ResponseEntity<?> updateCurrentUser(@Valid @RequestBody UserRequest request,
                                               BindingResult bindingResult,
                                               Authentication auth) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        return ResponseEntity.ok(userService.updateUserByEmail(auth.getName(), request));
    }
}

