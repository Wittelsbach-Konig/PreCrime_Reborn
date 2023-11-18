package ru.itmo.precrimeupd.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itmo.precrimeupd.dto.PreCogDto;
import ru.itmo.precrimeupd.exceptions.NotFoundException;
import ru.itmo.precrimeupd.exceptions.NotValidArgumentException;
import ru.itmo.precrimeupd.model.PreCog;
import ru.itmo.precrimeupd.model.Role;
import ru.itmo.precrimeupd.model.UserEntity;
import ru.itmo.precrimeupd.repository.PreCogRepository;
import ru.itmo.precrimeupd.repository.UserRepository;
import ru.itmo.precrimeupd.security.SecurityUtil;
import ru.itmo.precrimeupd.service.impl.PreCogServiceImpl;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PreCogServiceTests {

    @Mock
    private PreCogRepository preCogRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StatisticService statisticService;
    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private PreCogServiceImpl preCogService;


    private PreCog preCog1;

    @BeforeEach
    void init(){
        preCog1 = PreCog.builder()
                .id(1L)
                .age(22)
                .commissionedOn(LocalDateTime.now())
                .dopamineLevel(100)
                .preCogName("Max")
                .serotoninLevel(100)
                .stressLevel(0)
                .isWork(true)
                .build();
    }


    @Test
    public void PreCogService_GetAllPreCogs_ReturnsListOfPreCogs(){
        PreCog preCog2 = PreCog.builder()
                .id(2L)
                .preCogName("Clare")
                .age(25)
                .stressLevel(0)
                .serotoninLevel(100)
                .dopamineLevel(100)
                .commissionedOn(LocalDateTime.now())
                .isWork(true)
                .build();
        List<PreCog> preCogs = Arrays.asList(preCog1,preCog2);
        when(preCogRepository.findAll()).thenReturn(preCogs);
        List<PreCog> result = preCogService.getAllPreCogs();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void PreCogService_GetPreCog_ReturnPreCog(){
        Long preCogId = 1L;
        when(preCogRepository.findById(preCogId)).thenReturn(Optional.of(preCog1));
        PreCog resultValue = preCogService.getPreCog(preCogId);
        Assertions.assertNotNull(resultValue);
        Assertions.assertEquals(preCog1, resultValue);
    }

    @Test
    public void PreCogService_GetPreCog_ReturnNull(){
        Long preCogId = 3L;
        when(preCogRepository.findById(preCogId)).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(NotFoundException.class, ()-> {
            PreCog resultValue = preCogService.getPreCog(preCogId);
        });
    }

    @Test
    public void PreCogService_AddNewPreCog_ReturnPreCog() {
        // Arrange
        PreCogDto preCogToAdd = new PreCogDto();
        preCogToAdd.setPreCogName("Max");
        preCogToAdd.setAge(22);
        when(preCogRepository.save(Mockito.any(PreCog.class))).thenReturn(preCog1);
        // Act
        PreCog addedPreCog = preCogService.addNewPreCog(preCogToAdd);
        // Assert
        Assertions.assertNotNull(addedPreCog);
        Assertions.assertEquals("Max", addedPreCog.getPreCogName());
    }

    @Test
    public void PreCogService_DeletePreCog_ReturnVoid(){
        Long preCogId = 1L;
        when(preCogRepository.findById(preCogId)).thenReturn(Optional.ofNullable(preCog1));
        doNothing().when(preCogRepository).delete(preCog1);
        Assertions.assertAll(()->preCogService.deletePreCog(preCogId));
    }

    @Test
    public void PreCogService_UpdatePreCogInfo_ReturnPreCog(){
        PreCogDto preCogToUpd = new PreCogDto();
        preCogToUpd.setPreCogName("Alex");
        preCogToUpd.setAge(24);
        Long preCogId = 1L;
        when(preCogRepository.findById(preCogId)).thenReturn(Optional.ofNullable(preCog1));
        when(preCogRepository.save(preCog1)).thenReturn(preCog1);
        PreCog updatedPreCog = preCogService.updatePreCogInfo(preCogId, preCogToUpd);
        Assertions.assertNotNull(updatedPreCog);
        Assertions.assertEquals(preCogToUpd.getPreCogName(), updatedPreCog.getPreCogName());
        Assertions.assertEquals(preCogToUpd.getAge(), updatedPreCog.getAge());
    }

    @Test
    public void PreCogService_RetirePreCog_ReturnPreCog() {
        Long preCogId = 1L;
        when(preCogRepository.findById(preCogId)).thenReturn(Optional.ofNullable(preCog1));
        when(preCogRepository.save(preCog1)).thenReturn(preCog1);

        PreCog updatedPreCog = preCogService.retirePreCog(preCogId);
        Assertions.assertNotNull(updatedPreCog);
        Assertions.assertEquals(false, updatedPreCog.isWork());
    }

    @Test
    public void PreCogService_RehabilitatePreCog_ReturnPreCog() {
        Long preCogId = 1L;
        when(preCogRepository.findById(preCogId)).thenReturn(Optional.ofNullable(preCog1));
        when(preCogRepository.save(preCog1)).thenReturn(preCog1);

        PreCog updatedPreCog = preCogService.rehabilitatePreCog(preCogId);
        Assertions.assertNotNull(updatedPreCog);
        Assertions.assertEquals(true, updatedPreCog.isWork());
    }

    @Test
    public void PreCogService_UpdateVitalSigns_ReturnVoid() {
        PreCog preCog2 = PreCog.builder()
                .id(2L)
                .preCogName("Clare")
                .age(25)
                .stressLevel(0)
                .serotoninLevel(100)
                .dopamineLevel(100)
                .commissionedOn(LocalDateTime.now())
                .isWork(true)
                .build();
        PreCog preCog3 = PreCog.builder()
                .id(3L)
                .preCogName("David")
                .age(24)
                .stressLevel(0)
                .serotoninLevel(100)
                .dopamineLevel(100)
                .commissionedOn(LocalDateTime.now())
                .isWork(true)
                .build();
        List<PreCog> preCogs = Arrays.asList(preCog1,preCog2, preCog3);
        when(preCogRepository.findAllByIsWorkTrue()).thenReturn(preCogs);
        when(preCogRepository.save(preCog1)).thenReturn(preCog1);
        int preCog1_serotonin = preCog1.getSerotoninLevel();
        int preCog2_serotonin = preCog2.getSerotoninLevel();
        int preCog3_serotonin = preCog3.getSerotoninLevel();
        int preCog1_dopamine = preCog1.getDopamineLevel();
        int preCog2_dopamine = preCog2.getDopamineLevel();
        int preCog3_dopamine = preCog3.getDopamineLevel();
        int preCog1_stress = preCog1.getStressLevel();
        int preCog2_stress = preCog2.getStressLevel();
        int preCog3_stress = preCog3.getStressLevel();
        preCogService.updateVitalSigns();
        Assertions.assertNotEquals(preCog1_dopamine,preCog1.getDopamineLevel());
        Assertions.assertNotEquals(preCog2_dopamine,preCog2.getDopamineLevel());
        Assertions.assertNotEquals(preCog3_dopamine,preCog3.getDopamineLevel());
        Assertions.assertNotEquals(preCog1_serotonin,preCog1.getSerotoninLevel());
        Assertions.assertNotEquals(preCog2_serotonin,preCog2.getSerotoninLevel());
        Assertions.assertNotEquals(preCog3_serotonin,preCog3.getSerotoninLevel());
        Assertions.assertNotEquals(preCog1_stress,preCog1.getStressLevel());
        Assertions.assertNotEquals(preCog2_stress,preCog2.getStressLevel());
        Assertions.assertNotEquals(preCog3_stress,preCog3.getStressLevel());
    }

    @Test
    public void PreCogService_EnterDopamaine_NormalScenario() {
        PreCog preCog2 = PreCog.builder()
                .id(2L)
                .preCogName("Clare")
                .age(25)
                .stressLevel(0)
                .serotoninLevel(100)
                .dopamineLevel(90)
                .commissionedOn(LocalDateTime.now())
                .isWork(true)
                .build();

        Role userRole3 = new Role();
        userRole3.setName("TECHNIC");
        UserEntity technic = new UserEntity();
        technic.setLogin("waLLess");
        technic.setPassword("you_shall_not_pass");
        technic.setFirstName("Wally");
        technic.setLastName("Walles");
        technic.setRoles(Collections.singleton(userRole3));
        technic.setTelegramId(49552);
        Long preCogId = 2L;
        int amountToEnter = 7;
        when(preCogRepository.findById(preCogId)).thenReturn(Optional.ofNullable(preCog2));
        when(securityUtil.getSessionUser()).thenReturn("waLLess");
        when(userRepository.findByLogin("waLLess")).thenReturn(technic);
        doNothing().when(statisticService).enteredDopamine(technic, amountToEnter);

        preCogService.enterDopamine(preCogId, amountToEnter);
        Assertions.assertEquals(97, preCog2.getDopamineLevel());
    }

    @Test
    public void PreCogService_EnterDopamine_ThrowException(){
        PreCog preCog2 = PreCog.builder()
                .id(2L)
                .preCogName("Clare")
                .age(25)
                .stressLevel(0)
                .serotoninLevel(100)
                .dopamineLevel(90)
                .commissionedOn(LocalDateTime.now())
                .isWork(true)
                .build();
        Role userRole3 = new Role();
        userRole3.setName("TECHNIC");
        UserEntity technic = new UserEntity();
        technic.setLogin("waLLess");
        technic.setPassword("you_shall_not_pass");
        technic.setFirstName("Wally");
        technic.setLastName("Walles");
        technic.setRoles(Collections.singleton(userRole3));
        technic.setTelegramId(49552);
        Long preCogId = 2L;
        int amountToEnter = 100;
        when(preCogRepository.findById(preCogId)).thenReturn(Optional.ofNullable(preCog2));
        Assertions.assertThrows(NotValidArgumentException.class, ()->{
            preCogService.enterDopamine(preCogId, amountToEnter);
        });

    }
}
