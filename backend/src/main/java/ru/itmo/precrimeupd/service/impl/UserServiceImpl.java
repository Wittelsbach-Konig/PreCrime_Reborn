package ru.itmo.precrimeupd.service.impl;


import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itmo.precrimeupd.dto.RegistrationDto;
import ru.itmo.precrimeupd.model.Role;
import ru.itmo.precrimeupd.model.UserEntity;
import ru.itmo.precrimeupd.repository.RoleRepository;
import ru.itmo.precrimeupd.repository.UserRepository;
import ru.itmo.precrimeupd.service.StatisticService;
import ru.itmo.precrimeupd.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private StatisticService statisticService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository
            , RoleRepository roleRepository
            , PasswordEncoder passwordEncoder
            , StatisticService statisticService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.statisticService = statisticService;
    }

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        UserEntity user = new UserEntity();
        user.setLogin(registrationDto.getLogin());
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        if (registrationDto.getEmail() == null || !registrationDto.getEmail().isEmpty()){
            user.setEmail(registrationDto.getEmail());
        }
        if(registrationDto.getAddress() == null || !registrationDto.getAddress().isEmpty()){
            user.setAddress(registrationDto.getAddress());
        }
        user.setTelegramId(registrationDto.getTelegramId());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        Set<Role> userRole = new HashSet<>();
        for (String roleName : registrationDto.getRoles()) {
            Role role = roleRepository.findByName(roleName);
            if (role != null){
                userRole.add(role);
            }
        }
        user.setRoles(userRole);
        userRepository.save(user);
        statisticService.createNewStatisticRecordRegistration(user.getLogin());
    }

    @Override
    public void updateUser(Long id, RegistrationDto updatedUserDto) {
        UserEntity userToUpdate = userRepository.findById(id).get();
        userToUpdate.setLogin(updatedUserDto.getLogin());
        userToUpdate.setPassword(updatedUserDto.getPassword());
        userToUpdate.setEmail(updatedUserDto.getEmail());
        userToUpdate.setFirstName(updatedUserDto.getFirstName());
        userToUpdate.setLastName(updatedUserDto.getLastName());
        userToUpdate.setAddress(updatedUserDto.getAddress());
        Set<Role> userRole = new HashSet<>();
        for (String roleName : updatedUserDto.getRoles()) {
            Role role = roleRepository.findByName(roleName);
            if (role != null){
                userRole.add(role);
            }
        }
        if(!userRole.isEmpty()) {
            userToUpdate.setRoles(userRole);
        }
        userRepository.save(userToUpdate);
    }

    // For ADMIN
    @Override
    public void deleteUser(Long userId) {
        Optional<UserEntity> userToDelete = userRepository.findById(userId);
        if(userToDelete.isPresent()){
            userRepository.deleteById(userId);
        }
    }

    // For ADMIN
    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public UserEntity findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public UserEntity findById(Long id) {
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isPresent()){
            return user.get();
        }
        return null;
    }
}
