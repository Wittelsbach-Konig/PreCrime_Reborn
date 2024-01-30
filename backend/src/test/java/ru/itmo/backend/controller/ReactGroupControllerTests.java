package ru.itmo.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.itmo.backend.controllers.ReactGroupController;
import ru.itmo.backend.dto.*;
import ru.itmo.backend.models.*;
import ru.itmo.backend.service.CardService;
import ru.itmo.backend.service.GroupResourceService;
import ru.itmo.backend.service.ReactGroupService;
import ru.itmo.backend.service.StatisticService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = ReactGroupController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@TestPropertySource("/application-test.properties")
public class ReactGroupControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReactGroupService reactGroupService;

    @MockBean
    private CardService cardService;

    @MockBean
    private GroupResourceService groupResourceService;

    @MockBean
    private StatisticService statisticService;

    @Test
    public void ReactGroupController_GetAllGroups_ReturnsListReactGroup() throws Exception{
        ReactGroup member1 = ReactGroup.builder()
                .id(1L)
                .memberName("Jack Black")
                .telegramId(47692)
                .build();
        ReactGroup member2 = ReactGroup.builder()
                .id(2L)
                .memberName("Joe Silverhand")
                .telegramId(73154)
                .build();
        ReactGroup member3 = ReactGroup.builder()
                .id(3L)
                .memberName("Daniel Ramires")
                .telegramId(85413)
                .build();
        List<ReactGroup> groupList = Arrays.asList(member1, member2, member3);
        when(reactGroupService.getAllMembers()).thenReturn(groupList);
        ResultActions resultActions = mockMvc.perform(get("/api/v1/reactiongroup/all")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void ReactGroupController_AddNewMan_ReturnsReactGroupDto() throws Exception{
        ReactGroupOutDto memberToCreate = new ReactGroupOutDto();
        memberToCreate.setMemberName("Jack Black");
        memberToCreate.setTelegramId(47692);
        memberToCreate.setInOperation(true);
        when(reactGroupService.createNewGroupMember(Mockito.any(ReactGroupInDto.class))).thenReturn(memberToCreate);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/reactiongroup/newman")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberToCreate)).characterEncoding("UTF-8"));
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.memberName", CoreMatchers.is(memberToCreate.getMemberName())));
    }

    @Test
    public void ReactGroupController_GetReactGroup_ReturnsReactGroupDto() throws Exception {
        Long memberId = 1L;
        ReactGroup member1 = ReactGroup.builder()
                .id(1L)
                .memberName("Jack Black")
                .telegramId(47692)
                .build();
        when(reactGroupService.findGroupMemberById(memberId)).thenReturn(member1);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/reactiongroup/{id}", memberId)
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.memberName", CoreMatchers.is(member1.getMemberName())));
    }

//    @Test
//    public void ReactGroupController_DeleteGroupMember_ReturnsString() throws Exception {
//        Long memberId = 1L;
//        doNothing().when(reactGroupService).deleteGroupMember(memberId);
//        ResultActions resultActions = mockMvc.perform(delete("/api/v1/reactiongroup/{id}", memberId));
//        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent())
//                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
//                .andExpect(MockMvcResultMatchers.content().string("Group member successfully deleted"));
//    }

    @Test
    public void ReactGroupController_RetireGroupMember_ReturnsReactGroupDto() throws Exception {
        Long memberId = 3L;
        ReactGroupOutDto memberToRetire = new ReactGroupOutDto();
        memberToRetire.setMemberName("Sam Fisher");
        memberToRetire.setTelegramId(99999);
        memberToRetire.setInOperation(false);
        when(reactGroupService.retireGroupMember(memberId)).thenReturn(memberToRetire);
        ResultActions resultActions = mockMvc.perform(put("/api/v1/reactiongroup/{id}/retire", memberId)
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inOperation", CoreMatchers.is(memberToRetire.isInOperation())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inOperation").value(false));
    }

    @Test
    public void ReactGroupController_UpdateGroupMemberInfo_ReturnsReactGroupDto() throws Exception {

        ReactGroupInDto memberToUpdate = new ReactGroupInDto();
        memberToUpdate.setMemberName("Sam Fisher");
        memberToUpdate.setTelegramId(99999);


        ReactGroupOutDto memberToUpdateRes = new ReactGroupOutDto();
        memberToUpdateRes.setMemberName("Sam Fisher");
        memberToUpdateRes.setTelegramId(99999);
        memberToUpdateRes.setInOperation(true);
        Long memberId = 3L;
        when(reactGroupService.updateGroupMember(memberId, memberToUpdate)).thenReturn(memberToUpdateRes);
        ResultActions resultActions = mockMvc.perform(put("/api/v1/reactiongroup/{id}", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberToUpdate))
                .characterEncoding("UTF-8"));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.memberName", CoreMatchers.is(memberToUpdate.getMemberName())));

    }

    @Test
    public void ReactGroupController_OrderResource_ReturnsString() throws Exception {
        Long id = 1L;
        int amount = 10;
        doNothing().when(groupResourceService).orderResource(id,amount);
        ResultActions resultActions = mockMvc.perform(put("/api/v1/reactiongroup/supply/{id}", id)
                .param("amount", String.valueOf(amount)));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("Resource successfully ordered"));
    }

    @Test
    public void ReactGroupController_UpdateCriminalStatus_StatusCaught_ReturnsCriminalOutDto() throws Exception {
        Long criminalId = 1L;
        String newStatus = "CAUGHT";
        CriminalOutDto criminalOutDto = CriminalOutDto.builder()
                .id(1L)
                .weapon("gun")
                .location("City")
                .status("CAUGHT")
                .name("Criminal")
                .build();
        when(cardService.updateCriminalStatus(criminalId, CriminalStatus.CAUGHT)).thenReturn(criminalOutDto);
        ResultActions resultActions = mockMvc.perform(put("/api/v1/reactiongroup/criminal/{id}", criminalId)
                .param("status", newStatus));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("CAUGHT"));
    }

    @Test
    public void ReactGroupController_UpdateCriminalStatus_StatusEscaped_ReturnsCriminalOutDto() throws Exception {
        Long criminalId = 1L;
        String newStatus = "ESCAPED";
        CriminalOutDto criminalOutDto = CriminalOutDto.builder()
                .id(1L)
                .weapon("gun")
                .location("City")
                .status("ESCAPED")
                .name("Criminal")
                .build();
        when(cardService.updateCriminalStatus(criminalId, CriminalStatus.ESCAPED)).thenReturn(criminalOutDto);
        ResultActions resultActions = mockMvc.perform(put("/api/v1/reactiongroup/criminal/{id}", criminalId)
                .param("status", newStatus));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("ESCAPED"));
    }

    @Test
    public void ReactGroupController_GetAllCriminals_ReturnsListOfCriminals() throws Exception {
        CriminalOutDto criminal1 = CriminalOutDto.builder()
                .id(1L)
                .name("Criminal1")
                .status("NOT_CAUGHT")
                .location("City")
                .weapon("Gun")
                .build();
        CriminalOutDto criminal2 = CriminalOutDto.builder()
                .id(2L)
                .name("Criminal2")
                .weapon("Shotgun")
                .status("NOT_CAUGHT")
                .location("Countryside")
                .build();
        CriminalOutDto criminal3 = CriminalOutDto.builder()
                .id(3L)
                .name("Criminal3")
                .weapon("Knife")
                .status("NOT_CAUGHT")
                .location("Cottage")
                .build();
        List<CriminalOutDto> criminals = Arrays.asList(criminal1, criminal2, criminal3);
        when(cardService.getAllCriminals(CriminalStatus.NOT_CAUGHT)).thenReturn(criminals);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/reactiongroup/criminal")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void ReactGroupController_GetAllCriminals_ReturnsNoContent() throws Exception {
        List<CriminalOutDto> criminals = new ArrayList<>();
        when(cardService.getAllCriminals(CriminalStatus.NOT_CAUGHT)).thenReturn(criminals);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/reactiongroup/criminal")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void ReactGroupController_GetGroupStatistic_ReturnsGroupStatisticDto() throws Exception {
        Long memberId =1L;
        ReactGroupStatisticDto expectedStatistic = new ReactGroupStatisticDto();
        expectedStatistic.setCriminalsEscaped(1);
        expectedStatistic.setCriminalsCaught(5);
        expectedStatistic.setMemberName("Jack Black");
        expectedStatistic.setId(1L);
        expectedStatistic.setTelegramId(15142);
        expectedStatistic.setInOperation(true);
        when(statisticService.getGroupMemberStatistic(memberId)).thenReturn(expectedStatistic);
        ResultActions resultActions = mockMvc.perform(get("/api/v1/reactiongroup/{id}/statistic", memberId)
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.criminalsCaught",
                                                            CoreMatchers.is(expectedStatistic.getCriminalsCaught())));
    }

    @Test
    public void ReactGroupController_GetAllResources_ReturnsListGroupResource() throws Exception{
        GroupResource resource1 = GroupResource.builder()
                .id(1L)
                .resourceName("Glock")
                .amount(25)
                .maxPossibleAmount(100)
                .build();
        GroupResource resource2 = GroupResource.builder()
                .id(2L)
                .resourceName("AK-47")
                .amount(52)
                .maxPossibleAmount(150)
                .build();
        GroupResource resource3 = GroupResource.builder()
                .id(3L)
                .resourceName("USP-S")
                .amount(54)
                .maxPossibleAmount(100)
                .build();
        List<GroupResource> resources = Arrays.asList(resource1, resource2, resource3);
        when(groupResourceService.getAllResources()).thenReturn(resources);
        ResultActions resultActions = mockMvc.perform(get("/api/v1/reactiongroup/supply")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void ReactGroupController_GetAllResources_ReturnsNoContent() throws Exception{

        List<GroupResource> resources = new ArrayList<>();
        when(groupResourceService.getAllResources()).thenReturn(resources);
        ResultActions resultActions = mockMvc.perform(get("/api/v1/reactiongroup/supply")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void ReactGroupController_GetAllTransport_ReturnsListTransport() throws Exception {
        Transport transport1 = Transport.builder()
                .id(1L)
                .brand("UAZ")
                .model("Patriot")
                .condition(100)
                .inOperation(true)
                .maximum_fuel(30)
                .remaining_fuel(60)
                .build();
        Transport transport2 = Transport.builder()
                .id(2L)
                .brand("TANK")
                .model("T500")
                .condition(100)
                .inOperation(false)
                .maximum_fuel(45)
                .remaining_fuel(70)
                .build();
        Transport transport3 = Transport.builder()
                .id(3L)
                .brand("GAZ")
                .model("Tigr")
                .condition(100)
                .inOperation(true)
                .maximum_fuel(100)
                .remaining_fuel(136)
                .build();
        List<Transport> transports = Arrays.asList(transport1, transport2, transport3);
        when(groupResourceService.getAllTransport()).thenReturn(transports);
        ResultActions resultActions = mockMvc.perform(get("/api/v1/reactiongroup/transport")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void ReactGroupController_GetAllTransport_ReturnsNoContent() throws Exception {
        List<Transport> transports = new ArrayList<>();
        when(groupResourceService.getAllTransport()).thenReturn(transports);
        ResultActions resultActions = mockMvc.perform(get("/api/v1/reactiongroup/transport")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void ReactGroupController_GetResourcesByFilter_ReturnsListGroupResource() throws Exception {
        GroupResource resource1 = GroupResource.builder()
                .id(1L)
                .resourceName("Glock")
                .amount(25)
                .type(GroupResourceType.WEAPON)
                .maxPossibleAmount(100)
                .build();
        GroupResource resource2 = GroupResource.builder()
                .id(2L)
                .resourceName("AK-47")
                .amount(52)
                .type(GroupResourceType.WEAPON)
                .maxPossibleAmount(150)
                .build();
        GroupResource resource3 = GroupResource.builder()
                .id(3L)
                .resourceName("USP-S")
                .amount(54)
                .type(GroupResourceType.WEAPON)
                .maxPossibleAmount(100)
                .build();
        GroupResource resource4 = GroupResource.builder()
                .id(4L)
                .resourceName("Pistol bullets")
                .amount(25)
                .type(GroupResourceType.AMMUNITION)
                .maxPossibleAmount(100)
                .build();
        GroupResource resource5 = GroupResource.builder()
                .id(5L)
                .resourceName("Riffle bullets")
                .amount(52)
                .type(GroupResourceType.AMMUNITION)
                .maxPossibleAmount(150)
                .build();
        List<GroupResource> resources = Arrays.asList(resource1, resource2, resource3, resource4, resource5);
        List<String> resourceTypes = Arrays.asList("WEAPON", "AMMUNITION");
        when(groupResourceService.getResourcesByType(resourceTypes)).thenReturn(resources);
        ResultActions resultActions = mockMvc.perform(get("/api/v1/reactiongroup/supply/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resourceTypes)));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void ReactGroupController_GetResourcesByFilter_ReturnsNoContent() throws Exception {
        List<String> resourceTypes = new ArrayList<>();
        ResultActions resultActions = mockMvc.perform(get("/api/v1/reactiongroup/supply/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resourceTypes)));

        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void ReactGroupController_AppointGroupToCriminal_ReturnsString() throws Exception {
        List<Long> peopleIds = Arrays.asList(1L, 2L, 3L, 5L);
        Long criminalId = 2L;
        doNothing().when(reactGroupService).appointGroupToCriminal(criminalId, peopleIds);
        ResultActions resultActions = mockMvc.perform(post("/api/v1/reactiongroup/criminal/{id}", criminalId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(peopleIds)));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("React group successfully appointed"));
    }

    @Test
    public void ReactGroupController_AppointGroupToCriminal_ReturnsBadRequest() throws Exception {
        List<Long> peopleIds = new ArrayList<>();
        Long criminalId = 2L;
        ResultActions resultActions = mockMvc.perform(post("/api/v1/reactiongroup/criminal/{id}", criminalId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(peopleIds)));
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("React group cannot be empty"));
    }

    @Test
    public void ReactGroupController_GetCriminal_ReturnsCriminal() throws Exception {
        Long criminalId = 2L;
        CriminalOutDto criminal1 = CriminalOutDto.builder()
                .id(1L)
                .name("Criminal1")
                .status("NOT_CAUGHT")
                .location("City")
                .weapon("Gun")
                .build();
        when(cardService.getCriminalById(criminalId)).thenReturn(criminal1);
        ResultActions resultActions = mockMvc.perform(get("/api/v1/reactiongroup/criminal/{id}", criminalId)
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(criminal1.getName())));
    }

    @Test
    public void ReactGroupController_GetTransport_ReturnsTransport() throws Exception {
        Long transportId = 1L;
        Transport transport1 = Transport.builder()
                .id(1L)
                .brand("UAZ")
                .model("Patriot")
                .condition(100)
                .inOperation(true)
                .maximum_fuel(30)
                .remaining_fuel(60)
                .build();
        when(groupResourceService.findTransportById(transportId)).thenReturn(transport1);
        ResultActions resultActions = mockMvc.perform(get("/api/v1/reactiongroup/transport/{id}", transportId)
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand", CoreMatchers.is(transport1.getBrand())));
    }

    @Test
    public void ReactGroupController_GetResource_ReturnsGroupResource() throws Exception {
        Long resourceId = 1L;
        GroupResource resource1 = GroupResource.builder()
                .id(1L)
                .resourceName("Glock")
                .amount(25)
                .type(GroupResourceType.WEAPON)
                .maxPossibleAmount(100)
                .build();
        when(groupResourceService.findResourceById(resourceId)).thenReturn(resource1);
        ResultActions resultActions = mockMvc.perform(get("/api/v1/reactiongroup/supply/{id}", resourceId)
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.resourceName", CoreMatchers.is(resource1.getResourceName())));
    }

    @Test
    public void ReactGroupController_AddNewResource_ReturnsResourceDto() throws Exception {
        ResourceDto resourceDto = ResourceDto.builder()
                .id(1L)
                .resourceName("M4A1")
                .amount(54)
                .maxPossibleAmount(120)
                .type("WEAPON")
                .build();
        when(groupResourceService.addNewResource(Mockito.any(ResourceDto.class))).thenReturn(resourceDto);
        ResultActions resultActions = mockMvc.perform(post("/api/v1/reactiongroup/supply/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resourceDto)));
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.resourceName", CoreMatchers.is(resourceDto.getResourceName())));
    }

    @Test
    public void ReactGroupController_AddNewTransport_ReturnsTransportDto() throws Exception{
        TransportOutDto transport = TransportOutDto.builder()
                .id(1L)
                .brand("TANK")
                .model("T500")
                .current_fuel(70)
                .condition(100)
                .status(true)
                .maximum_fuel(70)
                .build();
        TransportDto transportDto = TransportDto.builder()
                .brand("TANK")
                .model("T500")
                .maximum_fuel(70)
                .build();
        when(groupResourceService.addNewTransport(Mockito.any(TransportDto.class))).thenReturn(transport);
        ResultActions resultActions = mockMvc.perform(post("/api/v1/reactiongroup/transport/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transportDto)));
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand", CoreMatchers.is(transport.getBrand())));
    }

    @Test
    public void ReactGroupController_RefuelTransport_ReturnsString() throws Exception {
        Long transportId = 1L;
        int amount = 20;
        doNothing().when(groupResourceService).refuelCar(transportId, amount);
        ResultActions resultActions = mockMvc.perform(put("/api/v1/reactiongroup/transport/{id}/refuel", transportId)
                .param("amount", String.valueOf(amount)));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("Transport refueled successfully"));
    }

    @Test
    public void ReactGroupController_RetireTransport_ReturnsTransportOutDto() throws Exception{
        Long transportId = 1L;
        TransportOutDto transport = TransportOutDto.builder()
                .id(1L)
                .brand("TANK")
                .model("T500")
                .current_fuel(45)
                .condition(100)
                .status(false)
                .maximum_fuel(70)
                .build();
        when(groupResourceService.retireTransport(transportId)).thenReturn(transport);
        ResultActions resultActions = mockMvc.perform(put("/api/v1/reactiongroup/transport/{id}/retire", transportId)
                                                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false));
    }

    @Test
    public void ReactGroupController_RehabilitateTransport_ReturnsTransportOutDto() throws Exception{
        Long transportId = 1L;
        TransportOutDto transport = TransportOutDto.builder()
                .id(1L)
                .brand("TANK")
                .model("T500")
                .current_fuel(45)
                .condition(100)
                .status(true)
                .maximum_fuel(70)
                .build();
        when(groupResourceService.retireTransport(transportId)).thenReturn(transport);
        ResultActions resultActions = mockMvc.perform(put("/api/v1/reactiongroup/transport/{id}/retire", transportId)
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(true));
    }


    @Test
    public void ReactGroupController_DeleteTransport_ReturnsString() throws Exception {
        Long transportId = 1L;
        doNothing().when(groupResourceService).deleteTransport(transportId);
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/reactiongroup/transport/{id}",transportId));
        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("Transport deleted successfully"));
    }
}
