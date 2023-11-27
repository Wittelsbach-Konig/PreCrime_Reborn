package ru.itmo.backend.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itmo.backend.dto.RegistrationDto;
import ru.itmo.backend.dto.UserOutDto;
import ru.itmo.backend.exceptions.NotFoundException;
import ru.itmo.backend.models.Role;
import ru.itmo.backend.models.UserEntity;
import ru.itmo.backend.repository.RoleRepository;
import ru.itmo.backend.repository.UserRepository;
import ru.itmo.backend.service.impl.UserServiceImpl;

import java.util.*;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private StatisticService statisticService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void UserService_SaveUser_ReturnsUserOutDto() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setFirstName("Username1");
        registrationDto.setConfirmPassword("password1");
        registrationDto.setLogin("user1_login");
        registrationDto.setRoles(Arrays.asList("TECHNIC"));
        registrationDto.setPassword("password1");
        registrationDto.setLastName("First");
        registrationDto.setTelegramId(14521);

        Role userRole1 = new Role();
        userRole1.setName("TECHNIC");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole1);

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("Username1");
        userEntity.setLogin("user1_login");
        userEntity.setPassword("password1");
        userEntity.setRoles(userRoles);
        userEntity.setLastName("First");
        userEntity.setTelegramId(14521);

        when(userService.findUserByLogin(Mockito.anyString())).thenReturn(null);
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("password1");
        when(roleRepository.findByName(Mockito.anyString())).thenReturn(userRole1);
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(userEntity);
        doNothing().when(statisticService).createNewStatisticRecordRegistration(Mockito.anyString());

        UserOutDto result = userService.saveUser(registrationDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(registrationDto.getLogin(), result.getLogin());
        Assertions.assertEquals(registrationDto.getPassword(), result.getPassword());
    }

    @Test
    public void UserService_UpdateUser_ReturnsUserOutDto() {
        Long userId = 1L;
        Role userRole1 = new Role();
        userRole1.setName("TECHNIC");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole1);
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("Username1");
        userEntity.setLogin("user1_login");
        userEntity.setPassword("password1");
        userEntity.setRoles(userRoles);
        userEntity.setLastName("First");
        userEntity.setTelegramId(14521);
        userEntity.setId(1L);

        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setFirstName("NewUsername1");
        registrationDto.setConfirmPassword("NewPassword1");
        registrationDto.setLogin("user1_login");
        registrationDto.setRoles(Arrays.asList("TECHNIC"));
        registrationDto.setPassword("NewPassword1");
        registrationDto.setLastName("First");
        registrationDto.setTelegramId(14521);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userService.findUserByLogin(registrationDto.getLogin())).thenReturn(null);
        when(roleRepository.findByName(Mockito.anyString())).thenReturn(userRole1);
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("NewPassword1");
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        UserOutDto updatedUser = userService.updateUser(userId, registrationDto);
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(registrationDto.getLogin(), updatedUser.getLogin());
    }

    @Test
    public void UserService_GetAllUsers_ReturnsListUserOutDto() {
        Role userRole1 = new Role();
        userRole1.setName("TECHNIC");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole1);
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setFirstName("Username1");
        userEntity1.setLogin("user1_login");
        userEntity1.setPassword("password1");
        userEntity1.setRoles(userRoles);
        userEntity1.setLastName("First");
        userEntity1.setTelegramId(14521);
        userEntity1.setId(1L);

        Role userRole2 = new Role();
        userRole1.setName("DETECTIVE");
        Set<Role> userRoles2 = new HashSet<>();
        userRoles2.add(userRole2);
        UserEntity userEntity2 = new UserEntity();
        userEntity2.setFirstName("Username2");
        userEntity2.setLogin("user2_login");
        userEntity2.setPassword("password2");
        userEntity2.setRoles(userRoles2);
        userEntity2.setLastName("Second");
        userEntity2.setTelegramId(14551);
        userEntity2.setId(2L);

        Role userRole3 = new Role();
        userRole1.setName("AUDITOR");
        Set<Role> userRoles3 = new HashSet<>();
        userRoles3.add(userRole3);
        UserEntity userEntity3 = new UserEntity();
        userEntity3.setFirstName("Username3");
        userEntity3.setLogin("user3_login");
        userEntity3.setPassword("password3");
        userEntity3.setRoles(userRoles3);
        userEntity3.setLastName("Third");
        userEntity3.setTelegramId(14471);
        userEntity3.setId(3L);

        List<UserEntity> userList = Arrays.asList(userEntity1, userEntity2, userEntity3);

        when(userRepository.findAll()).thenReturn(userList);

        List<UserOutDto> result = userService.getAllUsers();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(userList.size(), result.size());
    }

    @Test
    public void UserService_FindUserById_ThrowsException(){
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(NotFoundException.class, () ->{
            userService.findUserById(userId);
        });
    }

    @Test
    public void UserService_FindUserByLogin_ReturnsUserEntity(){
        String login = "user2_login";
        Role userRole2 = new Role();
        userRole2.setName("DETECTIVE");
        Set<Role> userRoles2 = new HashSet<>();
        userRoles2.add(userRole2);
        UserEntity userEntity2 = new UserEntity();
        userEntity2.setFirstName("Username2");
        userEntity2.setLogin("user2_login");
        userEntity2.setPassword("password2");
        userEntity2.setRoles(userRoles2);
        userEntity2.setLastName("Second");
        userEntity2.setTelegramId(14551);
        userEntity2.setId(2L);

        when(userRepository.findByLogin(login)).thenReturn(userEntity2);
        UserEntity foundUser = userService.findUserByLogin(login);
        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(userEntity2.getLogin(), foundUser.getLogin());
    }
}
