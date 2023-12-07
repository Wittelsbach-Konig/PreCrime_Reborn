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
import ru.itmo.backend.dto.ResourceDto;
import ru.itmo.backend.dto.TransportDto;
import ru.itmo.backend.dto.TransportOutDto;
import ru.itmo.backend.exceptions.NotValidArgumentException;
import ru.itmo.backend.models.*;
import ru.itmo.backend.repository.GroupResourceRepository;
import ru.itmo.backend.repository.TransportRepository;
import ru.itmo.backend.repository.UserRepository;
import ru.itmo.backend.security.SecurityUtil;
import ru.itmo.backend.service.impl.GroupResourceImpl;

import java.util.*;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GroupResourceServiceTests {
    @Mock
    private GroupResourceRepository groupResourceRepository;
    @Mock
    private TransportRepository transportRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StatisticService statisticService;
    @Mock
    private SecurityUtil securityUtil;


    @InjectMocks
    private GroupResourceImpl groupResourceService;

    private GroupResource resource1, resource2, resource3;
    private Transport transport1, transport2, transport3;

    @BeforeEach
    void init() {
        resource1 = GroupResource.builder()
                .id(1L)
                .resourceName("AK-47")
                .amount(100)
                .maxPossibleAmount(200)
                .type(GroupResourceType.WEAPON)
                .build();
        resource2 = GroupResource.builder()
                .id(2L)
                .resourceName("M4A1")
                .amount(50)
                .maxPossibleAmount(100)
                .type(GroupResourceType.WEAPON)
                .build();
        resource3 = GroupResource.builder()
                .id(3L)
                .resourceName("Riffle_bullets")
                .amount(1200)
                .maxPossibleAmount(3000)
                .type(GroupResourceType.AMMUNITION)
                .build();
        transport1 = Transport.builder()
                .id(1L)
                .brand("UAZ")
                .model("Patriot")
                .condition(100)
                .inOperation(true)
                .maximum_fuel(30)
                .remaining_fuel(60)
                .build();
        transport2 = Transport.builder()
                .id(2L)
                .brand("TANK")
                .model("T500")
                .condition(100)
                .inOperation(false)
                .maximum_fuel(45)
                .remaining_fuel(70)
                .build();
        transport3 = Transport.builder()
                .id(3L)
                .brand("GAZ")
                .model("Tigr")
                .condition(100)
                .inOperation(true)
                .maximum_fuel(100)
                .remaining_fuel(136)
                .build();
    }

    @Test
    public void GroupResourceService_GetAllResources_ReturnListOfGroupResource() {
        List<GroupResource> groupResources = Arrays.asList(resource1, resource2, resource3);
        when(groupResourceRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))).thenReturn(groupResources);
        List<GroupResource> result = groupResourceService.getAllResources();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(groupResources.size(), result.size());
    }

    @Test
    public void GroupResourceService_FindResourceById_ReturnsGroupResource(){
        Long resourceId = 1L;
        when(groupResourceRepository.findById(resourceId)).thenReturn(Optional.of(resource1));
        GroupResource returnValue = groupResourceService.findResourceById(resourceId);
        Assertions.assertNotNull(returnValue);
    }

    @Test
    public void GroupResourceService_GetResourcesByType_ReturnsListOfGroupResource(){
        List<String> types = new ArrayList<>();
        types.add("AMMUNITION");
        types.add("WEAPON");
        List<GroupResource> groupResourcesWeapon = Arrays.asList(resource1,resource2);
        List<GroupResource> groupResourcesAmmo = Arrays.asList(resource3);
        when(groupResourceRepository.findAllByType(GroupResourceType.WEAPON)).thenReturn(groupResourcesWeapon);
        when(groupResourceRepository.findAllByType(GroupResourceType.AMMUNITION)).thenReturn(groupResourcesAmmo);
        List<GroupResource> resultValue = groupResourceService.getResourcesByType(types);
        Assertions.assertNotNull(resultValue);
        Assertions.assertEquals(groupResourcesAmmo.size()+groupResourcesWeapon.size(),resultValue.size());
    }

    @Test
    public void GroupResourceService_AddNewResource_ReturnsGroupResource(){
        ResourceDto resourceToAdd = ResourceDto.builder()
                .resourceName("Glock 18")
                .amount(160)
                .maxPossibleAmount(300)
                .type("WEAPON")
                .build();

        GroupResource newResource = GroupResource.builder()
                .id(5L)
                .type(GroupResourceType.WEAPON)
                .resourceName("Glock 18")
                .maxPossibleAmount(300)
                .amount(160)
                .build();

        when(groupResourceRepository.findByResourceName(resourceToAdd.getResourceName())).thenReturn(null);
        when(groupResourceRepository.save(Mockito.any(GroupResource.class))).thenReturn(newResource);

        ResourceDto resultValue = groupResourceService.addNewResource(resourceToAdd);

        Assertions.assertNotNull(resultValue);
        Assertions.assertEquals(resourceToAdd.getResourceName(), resultValue.getResourceName());
    }

    @Test
    public void GroupResourceService_OrderResource_NormalScenario(){
        Role userRole = new Role();
        userRole.setName("REACT_GROUP");
        UserEntity bossReactGroup = new UserEntity();
        bossReactGroup.setLogin("HobbsLuke");
        bossReactGroup.setPassword("password");
        bossReactGroup.setFirstName("Luke");
        bossReactGroup.setLastName("Hobbs");
        bossReactGroup.setRoles(Collections.singleton(userRole));
        bossReactGroup.setTelegramId(49552);
        Long resourceId = 3L;
        int amountToAdd = 1500;
        int old_amount = resource3.getAmount();
        when(securityUtil.getSessionUser()).thenReturn("HobbsLuke");
        when(userRepository.findByLogin("HobbsLuke")).thenReturn(bossReactGroup);
        when(groupResourceRepository.findById(resourceId)).thenReturn(Optional.ofNullable(resource3));
        when(groupResourceRepository.save(resource3)).thenReturn(resource3);
        doNothing().when(statisticService).orderedResource(bossReactGroup,amountToAdd,resource3.getType());

        groupResourceService.orderResource(resourceId, amountToAdd);
        Assertions.assertEquals(old_amount + amountToAdd, resource3.getAmount());

    }

    @Test
    public void GroupResourceService_OrderResource_NegativeScenario(){
        Role userRole = new Role();
        userRole.setName("REACT_GROUP");
        UserEntity bossReactGroup = new UserEntity();
        bossReactGroup.setLogin("HobbsLuke");
        bossReactGroup.setPassword("password");
        bossReactGroup.setFirstName("Luke");
        bossReactGroup.setLastName("Hobbs");
        bossReactGroup.setRoles(Collections.singleton(userRole));
        bossReactGroup.setTelegramId(49552);
        Long resourceId = 3L;
        int amountToAdd = 150000;
        when(securityUtil.getSessionUser()).thenReturn("HobbsLuke");
        when(userRepository.findByLogin("HobbsLuke")).thenReturn(bossReactGroup);
        when(groupResourceRepository.findById(resourceId)).thenReturn(Optional.ofNullable(resource3));

        Assertions.assertThrows(NotValidArgumentException.class, () -> {
            groupResourceService.orderResource(resourceId, amountToAdd);
        });
    }

    @Test
    public void GroupResourceService_GetAllTransport_ReturnsListOfTransport() {
        List<Transport> transportList = Arrays.asList(transport1, transport2, transport3);
        when(transportRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(transportList);
        List<Transport> result = groupResourceService.getAllTransport();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(transportList.size(), result.size());
    }

    @Test
    void GroupResourceService_AddNewTransport_ReturnTransport() {
        TransportDto transportToAdd = TransportDto.builder()
                .brand("Aurus")
                .model("Arsenal")
                .maximum_fuel(80)
                .build();

        Transport transport5 = Transport.builder()
                .id(4L)
                .remaining_fuel(80)
                .maximum_fuel(80)
                .inOperation(true)
                .condition(100)
                .brand("Aurus")
                .model("Arsenal")
                .build();
        when(transportRepository.save(Mockito.any(Transport.class))).thenReturn(transport5);

        TransportOutDto result = groupResourceService.addNewTransport(transportToAdd);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(transportToAdd.getBrand(),result.getBrand());
    }

    @Test
    void GroupResourceService_RetireTransport_ReturnVoid() {
        Long transportId = 1L;
        when(transportRepository.findById(transportId)).thenReturn(Optional.ofNullable(transport1));
        when(transportRepository.save(transport1)).thenReturn(transport1);

        groupResourceService.retireTransport(transportId);

        Assertions.assertEquals(false, transport1.getInOperation());
    }

    @Test
    void GroupResourceService_RehabilitateTransport_ReturnVoid() {
        Long transportId = 2L;
        when(transportRepository.findById(transportId)).thenReturn(Optional.ofNullable(transport2));
        when(transportRepository.save(transport2)).thenReturn(transport2);

        groupResourceService.rehabilitateTransport(transportId);

        Assertions.assertEquals(true, transport2.getInOperation());
    }

    @Test
    void GroupResourceService_DeleteTransport_ReturnVoid() {
        Long transportId = 3L;
        when(transportRepository.findById(transportId)).thenReturn(Optional.ofNullable(transport3));
        doNothing().when(transportRepository).delete(transport3);
        Assertions.assertAll(()->{
            groupResourceService.deleteTransport(transportId);
        });
    }

}
