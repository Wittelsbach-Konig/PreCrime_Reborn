package ru.itmo.backend.repository;


import org.hibernate.TransientPropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import ru.itmo.backend.models.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.Collections;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CriminalRepositoryTests {
    @Autowired
    private CriminalRepository criminalRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private VisionRepository visionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Vision savedVision;
    private UserEntity savedDetective;
    private CrimeCard crimeCard;

    @BeforeEach
    public void init(){
        Vision crimeVision = new Vision();
        crimeVision.setVideoUrl("http://www.youtube.com/1");
        crimeVision.setAccepted(true);
        savedVision = visionRepository.save(crimeVision);

        Role userRole1 = new Role();
        userRole1.setName("DETECTIVE");
        Role savedRole = roleRepository.save(userRole1);

        UserEntity detective1 = new UserEntity();
        detective1.setLogin("sherDet");
        detective1.setPassword("password");
        detective1.setFirstName("Sherlock");
        detective1.setLastName("Holmes");
        detective1.setRoles(Collections.singleton(savedRole));
        detective1.setTelegramId(12456);
        savedDetective = userRepository.save(detective1);

        CrimeCard card = CrimeCard.builder()
                .criminalName("Joe Barbaro")
                .victimName("Tommy Angelo")
                .weapon("Shotgun")
                .placeOfCrime("Tommy's house")
                .crimeTime(LocalDateTime.now())
                .typeOfCrime(CrimeType.INTENTIONAL)
                .isCriminalCaught(false)
                .responsibleDetective(detective1)
                .vision(savedVision)
                .build();
        crimeCard = cardRepository.save(card);
    }

    @Test
    public void CriminalRepository_SaveCriminal_ReturnCriminal(){

        Criminal criminal = new Criminal();
        criminal.setStatus(CriminalStatus.NOT_CAUGHT);
        criminal.setCrimeCard(crimeCard);
        criminal.setLocation("Tommy's house");
        criminal.setWeapon("Shotgun");
        criminal.setName("Joe Barbaro");
        Criminal savedCriminal = criminalRepository.save(criminal);
        Assertions.assertNotNull(savedCriminal);
    }

    @Test
    public void CriminalRepository_SaveCriminalNonExistCard_ThrowsException(){
        CrimeCard notSavedCard = new CrimeCard();
        Criminal criminal = new Criminal();
        criminal.setStatus(CriminalStatus.NOT_CAUGHT);
        criminal.setCrimeCard(notSavedCard);
        criminal.setLocation("Tommy's house");
        criminal.setWeapon("Shotgun");
        criminal.setName("Joe Barbaro");
        Exception exception = Assertions.assertThrows(InvalidDataAccessApiUsageException.class, ()->{
            criminalRepository.save(criminal);
        });
        Assertions.assertTrue(exception.getCause().getCause() instanceof TransientPropertyValueException);
    }

    @Test
    public void CriminalRepository_SaveCriminalEmptyWeaponField_ThrowsException(){
        Criminal criminal = new Criminal();
        criminal.setStatus(CriminalStatus.NOT_CAUGHT);
        criminal.setCrimeCard(crimeCard);
        criminal.setLocation("Tommy's house");
        criminal.setWeapon("");
        criminal.setName("Joe Barbaro");
        Assertions.assertThrows(javax.validation.ConstraintViolationException.class, ()->{
            criminalRepository.save(criminal);
        });
    }

    @Test
    public void CriminalRepository_DeleteRelatedCrimeCard (){
        Criminal criminal = new Criminal();
        criminal.setStatus(CriminalStatus.NOT_CAUGHT);
        criminal.setCrimeCard(crimeCard);
        criminal.setLocation("Tommy's house");
        criminal.setWeapon("Shotgun");
        criminal.setName("Joe Barbaro");
        criminalRepository.save(criminal);
        Exception exception = Assertions.assertThrows(PersistenceException.class, ()->{
            cardRepository.delete(crimeCard);
            entityManager.flush();
            entityManager.clear();

        });
        Assertions.assertTrue(exception.getCause() instanceof ConstraintViolationException);

    }
}
