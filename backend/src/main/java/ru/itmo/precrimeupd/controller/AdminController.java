package ru.itmo.precrimeupd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.precrimeupd.dto.RegistrationDto;
import ru.itmo.precrimeupd.dto.UserOutDto;
import ru.itmo.precrimeupd.model.UserEntity;
import ru.itmo.precrimeupd.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
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
    public ResponseEntity<String> updateUserInfo(@PathVariable Long id
                                                , @RequestParam RegistrationDto updatedUser) {
        UserEntity user = userService.findById(id);
        UserEntity existingUserLogin = userService.findByLogin(user.getLogin());
        if(existingUserLogin != null
                && existingUserLogin.getLogin() != null
                && !existingUserLogin.getLogin().isEmpty()) {
            return new ResponseEntity<>("Update failed: User with this login already exists."
                    , HttpStatus.BAD_REQUEST);
        }
        userService.updateUser(id, updatedUser);
        return new ResponseEntity<>("User info successfully updated", HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("User successfully deleted", HttpStatus.NO_CONTENT);
    }

}
