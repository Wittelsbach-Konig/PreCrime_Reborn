package ru.itmo.precrimeupd.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmo.precrimeupd.dto.RegistrationDto;
import ru.itmo.precrimeupd.model.UserEntity;
import ru.itmo.precrimeupd.service.UserService;


@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ResponseEntity<String> loginPage(){
        return new ResponseEntity<>("auth/login", HttpStatus.OK) ;
    }

    @GetMapping("/registration")
    public ResponseEntity<RegistrationDto> getRegistrationPage() {
        return new ResponseEntity<>(new RegistrationDto(), HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationDto user,
                           BindingResult result) {
        UserEntity existingUserLogin = userService.findByLogin(user.getLogin());

        if(existingUserLogin != null
                && existingUserLogin.getLogin() != null
                && !existingUserLogin.getLogin().isEmpty()) {
            return new ResponseEntity<>("Registration failed: User with this login already exists."
                                        , HttpStatus.BAD_REQUEST);
        }
        if(result.hasErrors()){
            return new ResponseEntity<>("Registration failed: Validation error."
                                        , HttpStatus.BAD_REQUEST);
        }

        userService.saveUser(user);
        return new ResponseEntity<>("Registration successful", HttpStatus.OK);
    }
}
