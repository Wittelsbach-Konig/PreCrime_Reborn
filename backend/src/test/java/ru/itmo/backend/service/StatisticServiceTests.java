package ru.itmo.backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itmo.backend.dto.UserOutDto;
import ru.itmo.backend.dto.UserStatisticInfo;
import ru.itmo.backend.models.DetectiveStatistic;
import ru.itmo.backend.models.Role;
import ru.itmo.backend.models.TechnicStatistic;
import ru.itmo.backend.models.UserEntity;
import ru.itmo.backend.repository.*;
import ru.itmo.backend.service.impl.StatisticServiceImpl;

import java.util.*;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatisticServiceTests {
    @Mock
    private BossStatisticRepository bossStatisticRepository;
    @Mock
    private DetectiveStatisticRepository detectiveStatisticRepository;
    @Mock
    private ReactGroupStatisticRepository reactGroupStatisticRepository;
    @Mock
    private TechnicStatisticRepository technicStatisticRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReactGroupRepository reactGroupRepository;
    @Mock
    private CriminalToReactGroupRepository criminalToReactGroupRepository;

    @InjectMocks
    private StatisticServiceImpl statisticService;

    @Test
    public void StatisticService_GetAllUsers_ReturnsListUserOutDto(){
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
        List<UserOutDto> result = statisticService.getSystemWorkers();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(userList.size(), result.size());
    }

    @Test
    public void StatisticService_GetUserStatistic_ReturnsUserStatisticInfo() {
        Long userId = 1L;
        Role userRole1 = new Role();
        Role userRole2 = new Role();
        userRole1.setName("TECHNIC");
        userRole2.setName("DETECTIVE");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole1);
        userRoles.add(userRole2);
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setFirstName("Username1");
        userEntity1.setLogin("user1_login");
        userEntity1.setPassword("password1");
        userEntity1.setRoles(userRoles);
        userEntity1.setLastName("First");
        userEntity1.setTelegramId(14521);
        userEntity1.setId(1L);

        DetectiveStatistic detectiveStatistic = DetectiveStatistic.builder()
                .id(1L)
                .detective(userEntity1)
                .errorsInCards(4)
                .investigationCount(15)
                .build();
        TechnicStatistic technicStatistic = TechnicStatistic.builder()
                .id(2L)
                .depressantEntered(14)
                .dopamineEntered(50)
                .serotoninEntered(47)
                .visionsAccepted(4)
                .visionsRejected(1)
                .technic(userEntity1)
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(userEntity1));
        when(bossStatisticRepository.findByBoss(userEntity1)).thenReturn(null);
        when(technicStatisticRepository.findByTechnic(userEntity1)).thenReturn(technicStatistic);
        when(detectiveStatisticRepository.findByDetective(userEntity1)).thenReturn(detectiveStatistic);
        UserStatisticInfo result = statisticService.getUserStatistic(userId);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getUserInfo());
        Assertions.assertNotNull(result.getDetectiveStatistic());
        Assertions.assertNotNull(result.getTechnicStatistic());
        Assertions.assertEquals(Optional.empty(), result.getBossReactGroupStatistic());
    }

}
