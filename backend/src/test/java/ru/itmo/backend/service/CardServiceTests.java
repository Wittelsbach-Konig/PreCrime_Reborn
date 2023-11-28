package ru.itmo.backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itmo.backend.dto.CrimeCardInDto;
import ru.itmo.backend.dto.CrimeCardOutDto;
import ru.itmo.backend.dto.CriminalOutDto;
import ru.itmo.backend.exceptions.NotFoundException;
import ru.itmo.backend.models.*;
import ru.itmo.backend.repository.CardRepository;
import ru.itmo.backend.repository.CriminalRepository;
import ru.itmo.backend.repository.UserRepository;
import ru.itmo.backend.repository.VisionRepository;
import ru.itmo.backend.security.SecurityUtil;
import ru.itmo.backend.service.impl.CardServiceImpl;

import java.util.*;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardServiceTests {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private CriminalRepository criminalRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TelegramBotService telegramBotService;
    @Mock
    private StatisticService statisticService;
    @Mock
    private PreCogService preCogService;
    @Mock
    private SecurityUtil securityUtil;
    @Mock
    private VisionRepository visionRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    private UserEntity detective, detective2;
    private CrimeCard crimeCard;
    private CrimeCardOutDto crimeCardOutDto1;
    private Criminal criminal;
    private CrimeCardOutDto crimeCardOutDto;
    private Vision vision;

    @BeforeEach
    void init(){
        Role userRole1 = new Role();
        userRole1.setName("DETECTIVE");
        userRole1.setId(1L);
        detective = new UserEntity();
        detective.setLogin("sherDet");
        detective.setPassword("password");
        detective.setFirstName("Sherlock");
        detective.setLastName("Holmes");
        detective.setRoles(Collections.singleton(userRole1));
        detective.setTelegramId(12456);

        detective2 = new UserEntity();
        detective2.setLogin("underT0ne");
        detective2.setPassword("pass_word_pass");
        detective2.setFirstName("John");
        detective2.setLastName("Undertone");
        detective2.setRoles(Collections.singleton(userRole1));
        detective2.setTelegramId(45789);


        vision = new Vision();
        vision.setVideoUrl("http://www.youtube.com/");
        vision.setId(1L);
        crimeCard = CrimeCard.builder()
                .id(1L)
                .criminalName("criminal")
                .vision(vision)
                .responsibleDetective(detective)
                .typeOfCrime(CrimeType.INTENTIONAL)
                .placeOfCrime("City")
                .victimName("victim")
                .weapon("weapon")
                .crimeTime(null)
                .isCriminalCaught(false)
                .build();

        crimeCardOutDto = CrimeCardOutDto.builder()
                .responsibleDetective("Sherlock Holmes")
                .visionUrl("http://www.youtube.com/")
                .victimName("victim")
                .criminalName("criminal")
                .weapon("weapon")
                .build();
        criminal = new Criminal();
        criminal.setId(1L);
        criminal.setCrimeCard(crimeCard);
        criminal.setWeapon("weapon");
        criminal.setName("criminal");
        criminal.setLocation("City");
        criminal.setStatus(CriminalStatus.NOT_CAUGHT);
    }

    @Test
    public void CardService_CreateCard_ReturnsCrimeCardOutDto(){
        // Arrange
        Long visionId = 1L;

        when(securityUtil.getSessionUser()).thenReturn("sherDet");
        when(userRepository.findByLogin("sherDet")).thenReturn(detective);
        when(visionRepository.findById(visionId)).thenReturn(Optional.ofNullable(vision));
        when(cardRepository.save(Mockito.any(CrimeCard.class))).thenReturn(crimeCard);
        when(criminalRepository.save(Mockito.any(Criminal.class))).thenReturn(criminal);
        doNothing().when(preCogService).updateVitalSigns();
        doNothing().when(statisticService).completedInvestigation(detective);

        CrimeCardInDto cardInDto = CrimeCardInDto.builder()
                .id(1L)
                .criminalName("criminal")
                .victimName("victim")
                .weapon("weapon")
                .crimeType("INTENTIONAL")
                .placeOfCrime("City")
                .visionId(1L)
                .build();
        // Act
        CrimeCardOutDto createdCardDto = cardService.createCard(cardInDto);
        // Assert
        Assertions.assertNotNull(createdCardDto);
    }

    @Test
    public void CardServive_UpdateCard_ReturnsCrimeCardOutDto(){
        // Arrange
        Long cardId = 1L;
        CrimeCardInDto cardInDto = CrimeCardInDto.builder()
                .criminalName("criminal")
                .victimName("victim")
                .weapon("weapon")
                .crimeType("INTENTIONAL")
                .placeOfCrime("City")
                .build();
        when(cardRepository.findById(cardId)).thenReturn(Optional.ofNullable(crimeCard));
        when(criminalRepository.findByCrimeCard(crimeCard)).thenReturn(criminal);
        when(cardRepository.save(crimeCard)).thenReturn(crimeCard);
        when(criminalRepository.save(criminal)).thenReturn(criminal);
        // Act
        CrimeCardOutDto updatedCard = cardService.updateCard(cardId, cardInDto);
        // Assert
        Assertions.assertNotNull(updatedCard);
    }

    @Test
    public void CardService_GetAllCards_ReturnsListCardOutDto(){
        CrimeCard crimeCard1 = CrimeCard.builder()
                .id(1L)
                .criminalName("killer")
                .vision(vision)
                .responsibleDetective(detective)
                .typeOfCrime(CrimeType.INTENTIONAL)
                .placeOfCrime("City")
                .victimName("woman")
                .weapon("gun")
                .crimeTime(null)
                .isCriminalCaught(false)
                .build();
        CrimeCard crimeCard2 = CrimeCard.builder()
                .id(2L)
                .criminalName("Gardener")
                .vision(vision)
                .responsibleDetective(detective)
                .typeOfCrime(CrimeType.INTENTIONAL)
                .placeOfCrime("CountrySide")
                .victimName("Old man")
                .weapon("knife")
                .crimeTime(null)
                .isCriminalCaught(false)
                .build();
        List<CrimeCard> crimeCards = Arrays.asList(crimeCard1, crimeCard2);
        when(cardRepository.findAll()).thenReturn(crimeCards);
        List<CrimeCardOutDto> result = cardService.getAllCards();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void CardService_GetAllDetectiveCards_ReturnsListCardOutDto(){
        CrimeCard crimeCard1 = CrimeCard.builder()
                .id(1L)
                .criminalName("killer")
                .vision(vision)
                .responsibleDetective(detective)
                .typeOfCrime(CrimeType.INTENTIONAL)
                .placeOfCrime("City")
                .victimName("woman")
                .weapon("gun")
                .crimeTime(null)
                .isCriminalCaught(false)
                .build();
        CrimeCard crimeCard2 = CrimeCard.builder()
                .id(2L)
                .criminalName("Gardener")
                .vision(vision)
                .responsibleDetective(detective)
                .typeOfCrime(CrimeType.INTENTIONAL)
                .placeOfCrime("CountrySide")
                .victimName("Old man")
                .weapon("knife")
                .crimeTime(null)
                .isCriminalCaught(false)
                .build();
        CrimeCard crimeCard3 = CrimeCard.builder()
                .id(3L)
                .criminalName("Joe Barbara")
                .vision(vision)
                .responsibleDetective(detective)
                .typeOfCrime(CrimeType.INTENTIONAL)
                .placeOfCrime("Tommy's house")
                .victimName("Tommy Angelo")
                .weapon("shotgun")
                .crimeTime(null)
                .isCriminalCaught(false)
                .build();
        CrimeCard crimeCard4 = CrimeCard.builder()
                .id(4L)
                .criminalName("Max Payne")
                .vision(vision)
                .responsibleDetective(detective2)
                .typeOfCrime(CrimeType.INTENTIONAL)
                .placeOfCrime("Aesir Tower")
                .victimName("Nicole Horn")
                .weapon("Broadcast antenna")
                .crimeTime(null)
                .isCriminalCaught(false)
                .build();
        List<CrimeCard> crimeCardsDetective = Arrays.asList(crimeCard1, crimeCard2, crimeCard3);
        when(securityUtil.getSessionUser()).thenReturn("sherDet");
        when(userRepository.findByLogin("sherDet")).thenReturn(detective);
        when(cardRepository.findAllByResponsibleDetective(detective)).thenReturn(crimeCardsDetective);
        List<CrimeCardOutDto> result = cardService.getAllDetectiveCards();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.size());


    }

    @Test
    public void CardService_GetCardById_ReturnsCardOutDto() {
        Long cardToFindId = 1L;
        when(cardRepository.findById(cardToFindId)).thenReturn(Optional.of(crimeCard));
        CrimeCardOutDto crimeCardOutDto = cardService.getCardById(cardToFindId);

        Assertions.assertNotNull(crimeCardOutDto);
        Assertions.assertEquals("criminal",crimeCardOutDto.getCriminalName());
    }

    @Test
    public void CardService_GetCardById_ThrowsException() {
        Long cardToFindId = 2L;
        when(cardRepository.findById(cardToFindId)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(NotFoundException.class, ()->{
            CrimeCardOutDto crimeCardOutDto = cardService.getCardById(cardToFindId);
        });
    }

    @Test
    public void CardService_DeleteCardById_ReturnVoid(){
        Long cardId = 1L;
        when(cardRepository.findById(cardId)).thenReturn(Optional.ofNullable(crimeCard));
        doNothing().when(cardRepository).delete(crimeCard);
        Assertions.assertAll(()->cardService.deleteCard(cardId));
    }

    @Test
    public void CardService_UpdateCriminalStatus_CAUGHT() {
        Long criminalId = 1L;
        Criminal criminal1 = new Criminal();
        criminal1.setId(1L);
        criminal1.setCrimeCard(crimeCard);
        criminal1.setWeapon("weapon");
        criminal1.setName("criminal");
        criminal1.setLocation("City");
        criminal1.setStatus(CriminalStatus.CAUGHT);
        when(criminalRepository.findById(criminalId)).thenReturn(Optional.ofNullable(criminal));
        when(criminalRepository.save(criminal)).thenReturn(criminal1);

        CriminalOutDto updatedCriminalStatus = cardService.updateCriminalStatus(criminalId, CriminalStatus.CAUGHT);

        Assertions.assertNotNull(updatedCriminalStatus);
        Assertions.assertEquals("CAUGHT", updatedCriminalStatus.getStatus());
        Assertions.assertEquals(true, crimeCard.getIsCriminalCaught());
    }

    @Test
    public void CardService_UpdateCriminalStatus_ESCAPED() {
        Long criminalId = 1L;
        Criminal criminal1 = new Criminal();
        criminal1.setId(1L);
        criminal1.setCrimeCard(crimeCard);
        criminal1.setWeapon("weapon");
        criminal1.setName("criminal");
        criminal1.setLocation("City");
        criminal1.setStatus(CriminalStatus.ESCAPED);
        when(criminalRepository.findById(criminalId)).thenReturn(Optional.ofNullable(criminal));
        when(criminalRepository.save(criminal)).thenReturn(criminal1);

        CriminalOutDto updatedCriminalStatus = cardService.updateCriminalStatus(criminalId, CriminalStatus.ESCAPED);

        Assertions.assertNotNull(updatedCriminalStatus);
        Assertions.assertEquals("ESCAPED", updatedCriminalStatus.getStatus());
        Assertions.assertEquals(false, crimeCard.getIsCriminalCaught());
    }
}
