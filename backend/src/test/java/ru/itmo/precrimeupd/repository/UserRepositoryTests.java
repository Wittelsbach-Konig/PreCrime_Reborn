package ru.itmo.precrimeupd.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.itmo.precrimeupd.model.Role;
import ru.itmo.precrimeupd.model.UserEntity;

import java.util.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private Role userRole1, userRole2, userRole3, userRole4;

    @BeforeEach
    public void setup(){
        userRole1 = new Role();
        userRole2 = new Role();
        userRole3 = new Role();
        userRole4 = new Role();
        userRole1.setName("AUDITOR");
        userRole2.setName("DETECTIVE");
        userRole3.setName("TECHNIC");
        userRole4.setName("REACT_GROUP");
        roleRepository.save(userRole1);
        roleRepository.save(userRole2);
        roleRepository.save(userRole3);
        roleRepository.save(userRole4);
    }

    @Test
    public void UserRepository_SaveUser_ReturnSavedUser(){
        UserEntity detective1 = new UserEntity();
        detective1.setLogin("sherDet");
        detective1.setPassword("password");
        detective1.setFirstName("Sherlock");
        detective1.setLastName("Holmes");
        detective1.setRoles(Collections.singleton(userRole1));
        detective1.setTelegramId(12456);
        userRepository.save(detective1);

        Assertions.assertNotNull(detective1.getId());
        UserEntity savedUser = userRepository.findById(detective1.getId()).get();
        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals("sherDet",detective1.getLogin());
    }

    @Test
    public void UserRepository_GetAll_ReturnMoreThanOneUser(){
        UserEntity detective1 = new UserEntity();
        detective1.setLogin("sherDet");
        detective1.setPassword("password");
        detective1.setFirstName("Sherlock");
        detective1.setLastName("Holmes");
        detective1.setRoles(Collections.singleton(userRole1));
        detective1.setTelegramId(12456);
        userRepository.save(detective1);

        UserEntity detective2 = new UserEntity();
        detective2.setLogin("Undert0ne");
        detective2.setPassword("pass_win");
        detective2.setFirstName("John");
        detective2.setLastName("Undertone");
        detective2.setRoles(Collections.singleton(userRole1));
        detective2.setTelegramId(45881);
        userRepository.save(detective2);

        UserEntity technic = new UserEntity();
        technic.setLogin("waLLess");
        technic.setPassword("you_shall_not_pass");
        technic.setFirstName("Wally");
        technic.setLastName("Walles");
        technic.setRoles(Collections.singleton(userRole3));
        technic.setTelegramId(49552);
        userRepository.save(technic);

        List<UserEntity> userEntities = userRepository.findAll();
        Assertions.assertNotNull(userEntities);
        Assertions.assertEquals(3, userEntities.size());
    }

    @Test
    public void UserRepository_FindByLogin_ReturnUser(){
        UserEntity technic = new UserEntity();
        technic.setLogin("waLLess");
        technic.setPassword("you_shall_not_pass");
        technic.setFirstName("Wally");
        technic.setLastName("Walles");
        technic.setRoles(Collections.singleton(userRole3));
        technic.setTelegramId(49552);
        userRepository.save(technic);
        UserEntity foundUser = userRepository.findByLogin("waLLess");
        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals("Wally", foundUser.getFirstName());
    }

    @Test
    public void UserRepository_FindById_ReturnUser(){
        UserEntity detective2 = new UserEntity();
        detective2.setLogin("Undert0ne");
        detective2.setPassword("pass_win");
        detective2.setFirstName("John");
        detective2.setLastName("Undertone");
        detective2.setRoles(Collections.singleton(userRole1));
        detective2.setTelegramId(45881);
        userRepository.save(detective2);

        UserEntity foundUser = userRepository.findById(detective2.getId()).get();
        Assertions.assertNotNull(foundUser);
    }

    @Test
    public void UserRepository_UpdateUser_ReturnUserNotNull(){
        UserEntity detective2 = new UserEntity();
        detective2.setLogin("Undert0ne");
        detective2.setPassword("pass_win");
        detective2.setFirstName("John");
        detective2.setLastName("Undertone");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole1);
        detective2.setRoles(userRoles);
        detective2.setTelegramId(45881);
        userRepository.save(detective2);

        UserEntity userSave = userRepository.findById(detective2.getId()).get();
        userSave.setLastName("Wick");
        userSave.setTelegramId(48566);
        UserEntity updatedUser = userRepository.save(userSave);
        Assertions.assertNotNull(updatedUser.getLastName());
        Assertions.assertEquals("Wick", updatedUser.getLastName());
        Assertions.assertNotNull(updatedUser.getTelegramId());
        Assertions.assertEquals(48566,updatedUser.getTelegramId());
    }

    @Test
    public void UserRepository_DeleteUser_ReturnUserIsEmpty() {
        UserEntity detective1 = new UserEntity();
        detective1.setLogin("sherDet");
        detective1.setPassword("password");
        detective1.setFirstName("Sherlock");
        detective1.setLastName("Holmes");
        detective1.setRoles(Collections.singleton(userRole1));
        detective1.setTelegramId(12456);
        userRepository.save(detective1);

        userRepository.deleteById(detective1.getId());
        Optional<UserEntity> userReturn = userRepository.findById(detective1.getId());
        Assertions.assertEquals(true, userReturn.isEmpty());
    }
}
