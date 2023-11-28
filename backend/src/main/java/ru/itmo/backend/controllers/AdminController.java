package ru.itmo.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.itmo.backend.dto.RegistrationDto;
import ru.itmo.backend.dto.UserOutDto;
import ru.itmo.backend.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@Validated
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserOutDto>> getAllUsers() {
        List<UserOutDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserOutDto> getUser(@PathVariable Long id) {
        UserOutDto user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserOutDto> updateUserInfo(@PathVariable Long id
                                                    , @Valid @RequestBody RegistrationDto updatedUser) {
        UserOutDto userAfterUpdate = userService.updateUser(id, updatedUser);
        return new ResponseEntity<>(userAfterUpdate, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("User successfully deleted", HttpStatus.NO_CONTENT);
    }

}
