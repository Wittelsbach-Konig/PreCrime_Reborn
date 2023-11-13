package ru.itmo.precrimeupd.service;


import ru.itmo.precrimeupd.dto.RegistrationDto;
import ru.itmo.precrimeupd.dto.UserOutDto;
import ru.itmo.precrimeupd.model.UserEntity;

import java.util.List;

public interface UserService {
    void saveUser(RegistrationDto registrationDto);
    void updateUser(Long id, RegistrationDto updatedUserDto);
    void deleteUser(Long userId);
    List<UserOutDto> getAllUsers();
    UserEntity findByLogin(String login);
    UserEntity findById(Long id);
    UserOutDto getUserById(Long id);
}
