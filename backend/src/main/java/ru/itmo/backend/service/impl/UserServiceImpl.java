package ru.itmo.backend.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmo.backend.dto.RegistrationDto;
import ru.itmo.backend.dto.UserOutDto;
import ru.itmo.backend.exceptions.InvalidRoleException;
import ru.itmo.backend.exceptions.NotFoundException;
import ru.itmo.backend.exceptions.NotValidArgumentException;
import ru.itmo.backend.models.Role;
import ru.itmo.backend.models.UserEntity;
import ru.itmo.backend.repository.RoleRepository;
import ru.itmo.backend.repository.UserRepository;
import ru.itmo.backend.security.SecurityUtil;
import ru.itmo.backend.service.StatisticService;
import ru.itmo.backend.service.UserService;

import java.util.*;

import static ru.itmo.backend.mapper.UserEntitiyMapper.mapToUserOutDto;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;
    private final StatisticService statisticService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository
            , RoleRepository roleRepository
            , PasswordEncoder passwordEncoder
            , StatisticService statisticService
            , SecurityUtil securityUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.statisticService = statisticService;
        this.securityUtil = securityUtil;
    }

    private boolean isValidRole(Role role) {
        return Objects.equals(role.getName(), "ADMIN");
    }

    @Override
    public UserOutDto saveUser(RegistrationDto registrationDto) {
        UserEntity existingUserLogin = findUserByLogin(registrationDto.getLogin());
        if(!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            throw new NotValidArgumentException("Passwords don't matches!");
        }
        if(existingUserLogin != null
                && existingUserLogin.getLogin() != null
                && !existingUserLogin.getLogin().isEmpty()) {
            throw new NotValidArgumentException("Registration failed. User with login:\""
                                                + registrationDto.getLogin()
                                                + "\" already exists");
        }
        UserEntity user = new UserEntity();
        user.setLogin(registrationDto.getLogin());
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        if (registrationDto.getEmail() == null || !registrationDto.getEmail().isEmpty()){
            user.setEmail(registrationDto.getEmail());
        }
        user.setTelegramId(registrationDto.getTelegramId());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        Set<Role> userRole = new HashSet<>();
        for (String roleName : registrationDto.getRoles()) {
            Role role = roleRepository.findByName(roleName);
            if (role != null){
                if (isValidRole(role)){
                    throw new InvalidRoleException("Role ADMIN can't be taken.");
                }
                userRole.add(role);
            }
        }
        user.setRoles(userRole);
        UserEntity savedUser = userRepository.save(user);
        statisticService.createNewStatisticRecordRegistration(user.getLogin());
        return prepareUserForOutput(savedUser);
    }

    @Override
    public UserOutDto updateUser(Long id, RegistrationDto updatedUserDto) {
        UserEntity userToUpdate = findUserById(id);
        UserEntity existingUserLogin = findUserByLogin(userToUpdate.getLogin());
        if(existingUserLogin != null
                && existingUserLogin.getLogin() != null
                && !existingUserLogin.getLogin().isEmpty()) {
            throw new NotValidArgumentException("User with login \"" + userToUpdate.getLogin() + "\" already exists");
        }
        userToUpdate.setLogin(updatedUserDto.getLogin());
        userToUpdate.setPassword(passwordEncoder.encode(updatedUserDto.getPassword()));
        userToUpdate.setEmail(updatedUserDto.getEmail());
        userToUpdate.setFirstName(updatedUserDto.getFirstName());
        userToUpdate.setLastName(updatedUserDto.getLastName());
        Set<Role> userRole = new HashSet<>();
        for (String roleName : updatedUserDto.getRoles()) {
            Role role = roleRepository.findByName(roleName);
            if (role != null){
                if (isValidRole(role)){
                    throw new InvalidRoleException("Role ADMIN can't be taken.");
                }
                userRole.add(role);
            }
        }
        if(!userRole.isEmpty()) {
            userToUpdate.setRoles(userRole);
        }
        UserEntity updatedUser = userRepository.save(userToUpdate);
        return prepareUserForOutput(updatedUser);
    }

    // For ADMIN
    @Override
    public void deleteUser(Long userId) {
        String login = securityUtil.getSessionUser();
        UserEntity user = userRepository.findByLogin(login);
        if (Objects.equals(user.getId(), userId)) {
            throw new NotValidArgumentException("You can't delete yourself!");
        }
        UserEntity userToDelete = findUserById(userId);

        userRepository.delete(userToDelete);
    }

    // For ADMIN
    @Override
    public List<UserOutDto> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        List<UserOutDto> userOutDtos = new ArrayList<>();
        for(UserEntity userEntity : userEntities){
            UserOutDto tempUserDto = prepareUserForOutput(userEntity);
            userOutDtos.add(tempUserDto);
        }
        return userOutDtos;
    }


    @Override
    public UserEntity findUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public UserEntity findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    @Override
    public UserOutDto getUserById(Long id) {
        UserEntity user = findUserById(id);
        return prepareUserForOutput(user);
    }

    @Override
    public UserOutDto getUserByLogin(String login) {
        UserEntity user = findUserByLogin(login);
        return prepareUserForOutput(user);
    }

    private UserOutDto prepareUserForOutput(UserEntity userEntity){
        if(userEntity != null){
            UserOutDto tempUserDto = mapToUserOutDto(userEntity);
            tempUserDto.setLogin(userEntity.getLogin());
            Set<Role> roles = userEntity.getRoles();
            List<String> userRoles = new ArrayList<>();
            for(Role role : roles){
                userRoles.add(role.getName());
            }
            tempUserDto.setRoles(userRoles);
            return tempUserDto;
        }
        return null;
    }
}
