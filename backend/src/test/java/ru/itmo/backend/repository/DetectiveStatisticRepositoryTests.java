package ru.itmo.backend.repository;

import org.hibernate.TransientPropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import ru.itmo.backend.models.DetectiveStatistic;
import ru.itmo.backend.models.Role;
import ru.itmo.backend.models.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DetectiveStatisticRepositoryTests {
    @Autowired
    private DetectiveStatisticRepository detectiveStatisticRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void DetectiveStatisticRepository_CreateStatistic_ReturnsNotNull() {
        Role userRole2 = new Role();
        userRole2.setName("DETECTIVE");
        roleRepository.save(userRole2);
        UserEntity detective1 = new UserEntity();
        detective1.setLogin("sherDet");
        detective1.setPassword("password");
        detective1.setFirstName("Sherlock");
        detective1.setLastName("Holmes");
        detective1.setRoles(Collections.singleton(userRole2));
        detective1.setTelegramId(12456);
        userRepository.save(detective1);

        DetectiveStatistic detectiveStatistic = DetectiveStatistic.builder()
                .detective(detective1)
                .errorsInCards(1)
                .investigationCount(10)
                .build();
        detectiveStatisticRepository.save(detectiveStatistic);
        Assertions.assertNotNull(detectiveStatistic.getId());
        DetectiveStatistic savedStatistic = detectiveStatisticRepository.findById(detectiveStatistic.getId()).get();
        Assertions.assertNotNull(savedStatistic);
    }

    @Test
    public void DetectiveStatisticRepository_UpdateStatistic_ReturnsNotNull() {
        Role userRole2 = new Role();
        userRole2.setName("DETECTIVE");
        roleRepository.save(userRole2);
        UserEntity detective1 = new UserEntity();
        detective1.setLogin("sherDet");
        detective1.setPassword("password");
        detective1.setFirstName("Sherlock");
        detective1.setLastName("Holmes");
        detective1.setRoles(Collections.singleton(userRole2));
        detective1.setTelegramId(12456);
        userRepository.save(detective1);

        DetectiveStatistic detectiveStatistic = DetectiveStatistic.builder()
                .detective(detective1)
                .errorsInCards(1)
                .investigationCount(10)
                .build();
        detectiveStatisticRepository.save(detectiveStatistic);
        DetectiveStatistic statisticToUpdate = detectiveStatisticRepository.findById(detectiveStatistic.getId()).get();
        statisticToUpdate.setInvestigationCount(25);
        statisticToUpdate.setErrorsInCards(11);
        DetectiveStatistic updatedStatistic = detectiveStatisticRepository.save(statisticToUpdate);
        Assertions.assertNotNull(updatedStatistic);
        Assertions.assertEquals(statisticToUpdate.getInvestigationCount(), updatedStatistic.getInvestigationCount());
    }

    @Test
    public void DetectiveStatisticRepository_SaveDuplicatedStatisticsToOneDetective_ThrowException(){
        Role userRole2 = new Role();
        userRole2.setName("DETECTIVE");
        roleRepository.save(userRole2);
        UserEntity detective1 = new UserEntity();
        detective1.setLogin("sherDet");
        detective1.setPassword("password");
        detective1.setFirstName("Sherlock");
        detective1.setLastName("Holmes");
        detective1.setRoles(Collections.singleton(userRole2));
        detective1.setTelegramId(12456);
        userRepository.save(detective1);
        DetectiveStatistic detectiveStatistic = DetectiveStatistic.builder()
                .detective(detective1)
                .errorsInCards(1)
                .investigationCount(10)
                .build();
        detectiveStatisticRepository.save(detectiveStatistic);
        DetectiveStatistic detectiveStatistic1 = DetectiveStatistic.builder()
                .detective(detective1)
                .errorsInCards(25)
                .investigationCount(30)
                .build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            detectiveStatisticRepository.save(detectiveStatistic1);
        });
    }

    @Test
    public void DetectiveStatisticRepository_SaveStatisticToNonExistDetective_ThrowException(){
        Role userRole2 = new Role();
        userRole2.setName("DETECTIVE");
        roleRepository.save(userRole2);
        UserEntity detective1 = new UserEntity();
        detective1.setLogin("sherDet");
        detective1.setPassword("password");
        detective1.setFirstName("Sherlock");
        detective1.setLastName("Holmes");
        detective1.setRoles(Collections.singleton(userRole2));
        detective1.setTelegramId(12456);
        DetectiveStatistic detectiveStatistic = DetectiveStatistic.builder()
                .detective(detective1)
                .errorsInCards(1)
                .investigationCount(10)
                .build();
        Exception exception = Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            detectiveStatisticRepository.save(detectiveStatistic);
            entityManager.flush();
        });
        Assertions.assertTrue(exception.getCause().getCause() instanceof TransientPropertyValueException);

    }
}
