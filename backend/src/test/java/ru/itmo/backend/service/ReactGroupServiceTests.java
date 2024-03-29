package ru.itmo.backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.itmo.backend.dto.ReactGroupInDto;
import ru.itmo.backend.dto.ReactGroupOutDto;
import ru.itmo.backend.models.CrimeCard;
import ru.itmo.backend.models.Criminal;
import ru.itmo.backend.models.CriminalStatus;
import ru.itmo.backend.models.ReactGroup;
import ru.itmo.backend.repository.CriminalRepository;
import ru.itmo.backend.repository.CriminalToReactGroupRepository;
import ru.itmo.backend.repository.ReactGroupRepository;
import ru.itmo.backend.repository.UserRepository;
import ru.itmo.backend.security.SecurityUtil;
import ru.itmo.backend.service.impl.ReactGroupServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReactGroupServiceTests {
    @Mock
    private ReactGroupRepository reactGroupRepository;
    @Mock
    private TelegramBotService telegramBotService;
    @Mock
    private CriminalRepository criminalRepository;
    @Mock
    private StatisticService statisticService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CriminalToReactGroupRepository criminalToReactGroupRepository;
    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private ReactGroupServiceImpl reactGroupService;

    private ReactGroup member1, member2, member3;

    @BeforeEach
    void init(){
        member1 = ReactGroup.builder()
                .id(1L)
                .memberName("Jack Black")
                .telegramId(47692)
                .build();
        member2 = ReactGroup.builder()
                .id(2L)
                .memberName("Joe Silverhand")
                .telegramId(73154)
                .build();
        member3 = ReactGroup.builder()
                .id(3L)
                .memberName("Daniel Ramires")
                .telegramId(85413)
                .build();
    }

    @Test
    public void ReactGroupService_CreateNewGroupMember_ReturnReactGroupDto(){
        ReactGroupInDto memberToCreate = new ReactGroupInDto();
        memberToCreate.setMemberName("Jack Black");
        memberToCreate.setTelegramId(47692);

        when(reactGroupRepository.save(Mockito.any(ReactGroup.class))).thenReturn(member1);
        doNothing().when(statisticService).createNewStatisticRecordReactGroup(member1.getTelegramId());

        ReactGroupOutDto returnValue = reactGroupService.createNewGroupMember(memberToCreate);

        Assertions.assertNotNull(returnValue);
        Assertions.assertEquals(memberToCreate.getMemberName(),returnValue.getMemberName());
    }

    @Test
    public void ReactGroupService_FindGroupMemberById_ReturnReactGroup(){
        Long memberId = 1L;
        when(reactGroupRepository.findById(memberId)).thenReturn(Optional.of(member1));

        ReactGroup returnValue = reactGroupService.findGroupMemberById(memberId);

        Assertions.assertNotNull(returnValue);
    }

    @Test
    public void ReactGroupService_GetAllMembers_ReturnListOfReactGroup(){
        List<ReactGroup> reactGroups = Arrays.asList(member1, member2, member3);
        when(reactGroupRepository.findAll(Sort.by(Sort.Direction.DESC, "inOperation"))).thenReturn(reactGroups);
        List<ReactGroup> returnValue = reactGroupService.getAllMembers();

        Assertions.assertNotNull(returnValue);
        Assertions.assertEquals(reactGroups.size(), returnValue.size());
    }

//    @Test
//    public void ReactGroupService_DeleteMember_ReturnVoid(){
//        Long memberId = 2L;
//        when(reactGroupRepository.findById(memberId)).thenReturn(Optional.ofNullable(member2));
//        doNothing().when(reactGroupRepository).delete(member2);
//        Assertions.assertAll(()->{
//            reactGroupService.deleteGroupMember(memberId);
//        });
//
//    }

    @Test
    public void ReactGroupService_UpdateGroupMember_ReturnReactGroupDto(){
        ReactGroupInDto memberToUpdate = new ReactGroupInDto();
        memberToUpdate.setMemberName("Sam Fisher");
        memberToUpdate.setTelegramId(99999);
        Long memberId = 3L;
        when(reactGroupRepository.findById(memberId)).thenReturn(Optional.ofNullable(member3));
        when(reactGroupRepository.save(member3)).thenReturn(member3);
        ReactGroupOutDto returnValue = reactGroupService.updateGroupMember(memberId, memberToUpdate);

        Assertions.assertNotNull(returnValue);
        Assertions.assertEquals(memberToUpdate.getMemberName(), member3.getMemberName());
        Assertions.assertEquals(memberToUpdate.getTelegramId(), member3.getTelegramId());
    }

    @Test
    public void ReactGroupService_FindCriminalById_ReturnsCriminal(){
        CrimeCard card = CrimeCard.builder()
                .id(1L)
                .build();
        Long criminalId = 1L;
        Criminal criminal = new Criminal();
        criminal.setName("Criminal1");
        criminal.setWeapon("Gun");
        criminal.setCrimeCard(card);
        criminal.setLocation("City");
        criminal.setStatus(CriminalStatus.NOT_CAUGHT);
        criminal.setId(1L);

        when(criminalRepository.findById(criminalId)).thenReturn(Optional.of(criminal));

        Criminal foundCriminal = reactGroupService.findCriminalById(criminalId);
        Assertions.assertNotNull(foundCriminal);
        Assertions.assertEquals(criminal.getName(), foundCriminal.getName());
    }
}
