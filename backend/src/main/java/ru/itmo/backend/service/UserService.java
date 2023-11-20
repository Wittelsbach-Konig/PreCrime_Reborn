package ru.itmo.backend.service;


import ru.itmo.backend.dto.RegistrationDto;
import ru.itmo.backend.dto.UserOutDto;
import ru.itmo.backend.models.UserEntity;

import java.util.List;

public interface UserService {
    UserOutDto saveUser(RegistrationDto registrationDto);
    UserOutDto updateUser(Long id, RegistrationDto updatedUserDto);
    void deleteUser(Long userId);
    List<UserOutDto> getAllUsers();
    UserEntity findUserByLogin(String login);
    UserEntity findUserById(Long id);
    UserOutDto getUserById(Long id);
}
