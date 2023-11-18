package ru.itmo.precrimeupd.service;


import ru.itmo.precrimeupd.dto.RegistrationDto;
import ru.itmo.precrimeupd.dto.UserOutDto;
import ru.itmo.precrimeupd.model.UserEntity;

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
