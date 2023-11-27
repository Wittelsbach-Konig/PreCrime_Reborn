package ru.itmo.backend.repository;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.backend.models.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CardRepositoryTests {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private VisionRepository visionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private UserEntity detective1;
    private UserEntity detective2;
    private Vision crimeVision, crimeVision1, crimeVision2, crimeVision3;

    @BeforeEach
    @Transactional
    public void setup(){
        Role newRole = new Role();
        newRole.setName("DETECTIVE");
        roleRepository.save(newRole);

        Role userRole = roleRepository.findByName("DETECTIVE");

        detective1 = new UserEntity();
        detective1.setLogin("sherDet");
        detective1.setPassword("password");
        detective1.setFirstName("Sherlock");
        detective1.setLastName("Holmes");
        detective1.setRoles(Collections.singleton(userRole));
        detective1.setTelegramId(12456);
        userRepository.save(detective1);

        crimeVision = new Vision();
        crimeVision.setVideoUrl("https://www.youtube.com/0");
        visionRepository.save(crimeVision);
        crimeVision1 = new Vision();
        crimeVision1.setVideoUrl("https://www.youtube.com/1");
        visionRepository.save(crimeVision1);
        crimeVision2 = new Vision();
        crimeVision2.setVideoUrl("https://www.youtube.com/2");
        visionRepository.save(crimeVision2);
        crimeVision3 = new Vision();
        crimeVision3.setVideoUrl("https://www.youtube.com/3");
        visionRepository.save(crimeVision3);

        detective2 = new UserEntity();
        detective2.setLogin("Undert0ne");
        detective2.setPassword("pass_win");
        detective2.setFirstName("John");
        detective2.setLastName("Undertone");
        detective2.setRoles(Collections.singleton(userRole));
        detective2.setTelegramId(45881);
        userRepository.save(detective2);
    }


    @Test
    public void CardRepository_SaveCard_ValuesSame() {
        CrimeCard card = CrimeCard.builder()
                .criminalName("Joe Barbaro")
                .victimName("Tommy Angelo")
                .weapon("Shotgun")
                .placeOfCrime("Tommy's house")
                .crimeTime(LocalDateTime.now())
                .typeOfCrime(CrimeType.INTENTIONAL)
                .isCriminalCaught(false)
                .responsibleDetective(detective1)
                .vision(crimeVision)
                .build();

        cardRepository.save(card);
        Assertions.assertNotNull(card.getId());
        CrimeCard savedCard = cardRepository.findById(card.getId()).orElse(null);
        Assertions.assertNotNull(savedCard);
        Assertions.assertEquals("Tommy Angelo",savedCard.getVictimName());
        Assertions.assertEquals("Joe Barbaro", savedCard.getCriminalName());
        Assertions.assertEquals("Shotgun", savedCard.getWeapon());
        Assertions.assertEquals("Sherlock", savedCard.getResponsibleDetective().getFirstName());
    }

    @Test
    public void CardRepository_SaveCardWithoutVictim_ValidationError(){
        CrimeCard card = CrimeCard.builder()
                .criminalName("Joe Barbaro")
                .weapon("Shotgun")
                .placeOfCrime("Tommy's house")
                .crimeTime(LocalDateTime.now())
                .typeOfCrime(CrimeType.INTENTIONAL)
                .isCriminalCaught(false)
                .responsibleDetective(detective1)
                .vision(crimeVision)
                .build();
        Assertions.assertThrows(javax.validation.ConstraintViolationException.class, ()->{
            cardRepository.save(card);
            entityManager.flush();
        });
    }

    @Test
    public void CardRepository_GetAll_ReturnMoreThanOneCard() {
        CrimeCard card = CrimeCard.builder()
                .criminalName("Joe Barbaro")
                .victimName("Tommy Angelo")
                .weapon("Shotgun")
                .placeOfCrime("Tommy's house")
                .crimeTime(LocalDateTime.now())
                .typeOfCrime(CrimeType.INTENTIONAL)
                .isCriminalCaught(false)
                .responsibleDetective(detective1)
                .vision(crimeVision)
                .build();
        cardRepository.save(card);
        CrimeCard card1 = CrimeCard.builder()
                .criminalName("Radion Raskolnikov")
                .victimName("Staruha")
                .weapon("Axe")
                .placeOfCrime("Staruha's house")
                .crimeTime(LocalDateTime.now())
                .typeOfCrime(CrimeType.INTENTIONAL)
                .isCriminalCaught(false)
                .responsibleDetective(detective1)
                .vision(crimeVision1)
                .build();
        cardRepository.save(card1);

        List<CrimeCard> crimeCards = cardRepository.findAll();
        Assertions.assertNotNull(crimeCards);
        Assertions.assertEquals(2,crimeCards.size());
    }

    @Test
    public void CardRepository_FindAllByResponsibleDetective_ReturnsListCard(){
        CrimeCard card = CrimeCard.builder()
                .criminalName("Joe Barbaro")
                .victimName("Tommy Angelo")
                .weapon("Shotgun")
                .placeOfCrime("Tommy's house")
                .crimeTime(LocalDateTime.now())
                .typeOfCrime(CrimeType.INTENTIONAL)
                .isCriminalCaught(false)
                .responsibleDetective(detective1)
                .vision(crimeVision)
                .build();
        cardRepository.save(card);
        CrimeCard card1 = CrimeCard.builder()
                .criminalName("Radion Raskolnikov")
                .victimName("Staruha")
                .weapon("Axe")
                .placeOfCrime("Staruha's house")
                .crimeTime(LocalDateTime.now())
                .typeOfCrime(CrimeType.INTENTIONAL)
                .isCriminalCaught(false)
                .responsibleDetective(detective1)
                .vision(crimeVision1)
                .build();
        cardRepository.save(card1);
        CrimeCard card2 = CrimeCard.builder()
                .criminalName("Max Payne")
                .victimName("Nicole Horn")
                .weapon("Broadcast antenna")
                .placeOfCrime("Aesir plaza")
                .crimeTime(LocalDateTime.now())
                .typeOfCrime(CrimeType.INTENTIONAL)
                .isCriminalCaught(false)
                .responsibleDetective(detective1)
                .vision(crimeVision2)
                .build();
        cardRepository.save(card2);
        CrimeCard card3 = CrimeCard.builder()
                .criminalName("Alonzo Mendez")
                .victimName("Celine Henry")
                .weapon("Knife")
                .placeOfCrime("The Moors hills")
                .crimeTime(LocalDateTime.now())
                .typeOfCrime(CrimeType.INTENTIONAL)
                .isCriminalCaught(false)
                .responsibleDetective(detective2)
                .vision(crimeVision3)
                .build();
        cardRepository.save(card3);

        List<CrimeCard> cardsDetective1 = cardRepository.findAllByResponsibleDetective(detective1);
        List<CrimeCard> cardsDetective2 = cardRepository.findAllByResponsibleDetective(detective2);
        Assertions.assertNotNull(cardsDetective1);
        Assertions.assertNotNull(cardsDetective2);
        Assertions.assertEquals(3, cardsDetective1.size());
        Assertions.assertEquals(1, cardsDetective2.size());
    }

    @Test
    public void CardRepository_UpdateCard_ReturnCardNotNull(){
        CrimeCard card = CrimeCard.builder()
                .criminalName("Joe Barbaro")
                .victimName("Tommy Angelo")
                .weapon("Shotgun")
                .placeOfCrime("Tommy's house")
                .crimeTime(LocalDateTime.now())
                .typeOfCrime(CrimeType.INTENTIONAL)
                .isCriminalCaught(false)
                .responsibleDetective(detective1)
                .vision(crimeVision)
                .build();
        cardRepository.save(card);
        CrimeCard cardSave = cardRepository.findById(card.getId()).get();
        cardSave.setIsCriminalCaught(true);
        cardSave.setCriminalName("Vito Scaletta");
        CrimeCard updatedCard = cardRepository.save(cardSave);
        Assertions.assertNotNull(updatedCard.getCriminalName());
        Assertions.assertEquals(true, updatedCard.getIsCriminalCaught());
    }

    @Test
    public void CardRepository_DeleteResponsibleDetective_ThrowsConstraintException(){
        CrimeCard card = CrimeCard.builder()
                .criminalName("Joe Barbaro")
                .victimName("Tommy Angelo")
                .weapon("Shotgun")
                .placeOfCrime("Tommy's house")
                .crimeTime(LocalDateTime.now())
                .typeOfCrime(CrimeType.INTENTIONAL)
                .isCriminalCaught(false)
                .responsibleDetective(detective1)
                .vision(crimeVision)
                .build();
        cardRepository.save(card);

        Exception exception = Assertions.assertThrows(PersistenceException.class, ()->{
            userRepository.deleteById(detective1.getId());
            entityManager.flush();
        });
        Assertions.assertTrue(exception.getCause() instanceof ConstraintViolationException);
    }


    @Test
    public void CrimeCardRepository_Save2CardsWithSameVision_ThrowsException(){
        CrimeCard card = CrimeCard.builder()
                .criminalName("Joe Barbaro")
                .victimName("Tommy Angelo")
                .weapon("Shotgun")
                .placeOfCrime("Tommy's house")
                .crimeTime(LocalDateTime.now())
                .typeOfCrime(CrimeType.INTENTIONAL)
                .isCriminalCaught(false)
                .responsibleDetective(detective1)
                .vision(crimeVision)
                .build();
        cardRepository.save(card);
        CrimeCard card2 = CrimeCard.builder()
                .criminalName("Max Payne")
                .victimName("Nicole Horn")
                .weapon("Broadcast antenna")
                .placeOfCrime("Aesir plaza")
                .crimeTime(LocalDateTime.now())
                .typeOfCrime(CrimeType.INTENTIONAL)
                .isCriminalCaught(false)
                .responsibleDetective(detective1)
                .vision(crimeVision)
                .build();
        Assertions.assertThrows(DataIntegrityViolationException.class, ()->{
            cardRepository.save(card2);
        });
    }
}
