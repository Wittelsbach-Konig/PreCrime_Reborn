package ru.itmo.backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itmo.backend.dto.VisionDto;
import ru.itmo.backend.dto.VisionOutDto;
import ru.itmo.backend.models.Role;
import ru.itmo.backend.models.UserEntity;
import ru.itmo.backend.models.Vision;
import ru.itmo.backend.repository.UserRepository;
import ru.itmo.backend.repository.VisionRepository;
import ru.itmo.backend.security.SecurityUtil;
import ru.itmo.backend.service.impl.VisionServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VisionServiceTests {
    @Mock
    private VisionRepository visionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StatisticService statisticService;
    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private VisionServiceImpl visionService;

    private Vision vision1, vision2, vision3;

    @BeforeEach
    void init() {
        vision1 = Vision.builder()
                .id(1L)
                .videoUrl("http://www.youtube.com/1")
                .accepted(false)
                .build();
        vision2 = Vision.builder()
                .id(2L)
                .videoUrl("http://www.youtube.com/2")
                .accepted(false)
                .build();
        vision3 = Vision.builder()
                .id(3L)
                .videoUrl("http://www.youtube.com/3")
                .accepted(true)
                .build();
    }

    @Test
    public void VisionService_FindById_ReturnsVision(){
        Long visionId = 1L;
        when(visionRepository.findById(visionId)).thenReturn(Optional.of(vision1));
        Vision returnValue = visionService.findById(visionId);

        Assertions.assertNotNull(returnValue);
    }

    @Test
    public void VisionService_GetVisionsListAsTechnic_ReturnsListOfVision(){
        List<Vision> visionList = Arrays.asList(vision1, vision2, vision3);
        List<Vision> notAccepted = Arrays.asList(vision1, vision2);
        List<Vision> accepted = Arrays.asList(vision3);
        List<String> roles = Arrays.asList("TECHNIC");
        when(visionRepository.findAllByAcceptedFalse()).thenReturn(notAccepted);
        when(securityUtil.getSessionUserRoles()).thenReturn(roles);
        List<Vision> returnValue = visionService.getVisionsList();
        Assertions.assertNotNull(returnValue);
        Assertions.assertEquals(visionList.size() - accepted.size(), returnValue.size());
    }

    @Test
    public void VisionService_SaveVision_ReturnsVisionOutDto(){
        VisionDto visionToAdd = VisionDto.builder()
                .videoUrl("http://www.youtube.com/4")
                .build();
        Vision vision4 = Vision.builder()
                .id(4L)
                .videoUrl("http://www.youtube.com/4")
                .accepted(false)
                .build();

        when(visionRepository.save(Mockito.any(Vision.class))).thenReturn(vision4);
        VisionOutDto returnValue = visionService.saveVision(visionToAdd);
        Assertions.assertNotNull(returnValue);
        Assertions.assertEquals(visionToAdd.getVideoUrl(),returnValue.getVideoUrl());
        Assertions.assertEquals(false, returnValue.getAccepted());
    }

    @Test
    public void VisionService_DeleteVision_ReturnsVoid(){
        Long visionId = 1L;
        when(visionRepository.findById(visionId)).thenReturn(Optional.of(vision1));

        Role userRole = new Role();
        userRole.setName("TECHNIC");
        UserEntity technic = new UserEntity();
        technic.setLogin("waLLess");
        technic.setPassword("you_shall_not_pass");
        technic.setFirstName("Wally");
        technic.setLastName("Walles");
        technic.setRoles(Collections.singleton(userRole));
        technic.setTelegramId(49552);

        when(securityUtil.getSessionUser()).thenReturn("waLLess");
        when(userRepository.findByLogin("waLLess")).thenReturn(technic);
        doNothing().when(visionRepository).delete(vision1);
        doNothing().when(statisticService).visionRejected(technic);
        Assertions.assertAll(()->{
            visionService.deleteVision(visionId);
        });
    }

    @Test
    public void VisionService_ApproveVision_ReturnsVision(){
        Long visionId = 2L;
        when(visionRepository.findById(visionId)).thenReturn(Optional.ofNullable(vision2));
        Role userRole = new Role();
        userRole.setName("TECHNIC");
        UserEntity technic = new UserEntity();
        technic.setLogin("waLLess");
        technic.setPassword("you_shall_not_pass");
        technic.setFirstName("Wally");
        technic.setLastName("Walles");
        technic.setRoles(Collections.singleton(userRole));
        technic.setTelegramId(49552);

        when(securityUtil.getSessionUser()).thenReturn("waLLess");
        when(userRepository.findByLogin("waLLess")).thenReturn(technic);
        when(visionRepository.save(vision2)).thenReturn(vision2);
        doNothing().when(statisticService).visionAccepted(technic);
        visionService.approveVision(visionId);
        Assertions.assertEquals(true, vision2.isAccepted());
    }

}
