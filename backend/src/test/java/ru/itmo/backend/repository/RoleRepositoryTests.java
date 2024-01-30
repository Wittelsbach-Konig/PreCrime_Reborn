package ru.itmo.backend.repository;

import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.itmo.backend.models.Role;



@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RoleRepositoryTests {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void RoleRepository_SaveRole_ReturnsRole(){
        Role userRole1 = new Role();
        userRole1.setName("TECHNIC");
        Role savedRole = roleRepository.save(userRole1);
        Assertions.assertNotNull(savedRole);
        Assertions.assertNotNull(savedRole.getId());
    }

    @Test
    public void RoleRepository_SaveDuplicatedRoles_ThrowsException(){
        Role userRole1 = new Role();
        userRole1.setName("TECHNIC");
        roleRepository.save(userRole1);
        Role userRole2 = new Role();
        userRole2.setName("TECHNIC");
        Assertions.assertThrows(DataIntegrityViolationException.class, ()->{
            roleRepository.save(userRole2);
        });
    }

    @Test
    public void RoleRepository_SaveRoleWithNonValidName_ThrowsException(){
        Role userRole = new Role();
        userRole.setName("role_with_longest_name_ever_existed");
        Assertions.assertThrows(ConstraintViolationException.class, ()->{
            roleRepository.save(userRole);
        });
    }

    @Test
    public void RoleRepository_FindRoleByName_ReturnsRole(){
        Role userRole = new Role();
        userRole.setName("DETECTIVE");
        roleRepository.save(userRole);
        Role userRole1 = new Role();
        userRole1.setName("TECHNIC");
        roleRepository.save(userRole1);
        Role foundRole = roleRepository.findByName("DETECTIVE");
        Assertions.assertNotNull(foundRole);
        Role foundRole1 = roleRepository.findByName("TECHNIC");
        Assertions.assertNotNull(foundRole1);
    }
}
