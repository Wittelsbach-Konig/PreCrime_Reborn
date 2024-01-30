package ru.itmo.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import ru.itmo.backend.controllers.AuditorController;
import ru.itmo.backend.dto.*;
import ru.itmo.backend.service.CardService;
import ru.itmo.backend.service.StatisticService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = AuditorController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@TestPropertySource("/application-test.properties")
public class AuditorControllerTests {
    @MockBean
    private StatisticService statisticService;

    @MockBean
    private CardService cardService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void AuditorController_GetAllUsers_ReturnListUserOutDto() throws Exception{
        UserOutDto user1 = UserOutDto.builder()
                .id(1L)
                .login("user1_login")
                .firstName("Username1")
                .lastName("First")
                .telegramId(14521)
                .roles(Arrays.asList("TECHNIC"))
                .build();
        UserOutDto user2= UserOutDto.builder()
                .id(2L)
                .login("user2_login")
                .firstName("Username2")
                .lastName("Second")
                .telegramId(54782)
                .roles(Arrays.asList("DETECTIVE"))
                .build();
        UserOutDto user3 = UserOutDto.builder()
                .id(3L)
                .login("user3_login")
                .firstName("Username3")
                .lastName("Third")
                .telegramId(147785)
                .roles(Arrays.asList("AUDITOR"))
                .build();
        List<UserOutDto> userList = Arrays.asList(user1,user2, user3);
        when(statisticService.getSystemWorkers()).thenReturn(userList);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/auditor/users")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void AuditorController_GetAllCards_ReturnsListCards() throws Exception {
        CrimeCardOutDto cardOutDto = CrimeCardOutDto.builder()
                .id(1L)
                .weapon("weapon")
                .crimeTime("12-04-2023 22:15")
                .criminalName("criminal")
                .victimName("victim")
                .responsibleDetective("Sherlock Holmes")
                .visionUrl("http://www.youtube.com/1")
                .isCriminalCaught(false)
                .crimeType("INTENTIONAL")
                .placeOfCrime("City")
                .build();
        CrimeCardOutDto cardOutDto1 = CrimeCardOutDto.builder()
                .id(2L)
                .weapon("knife")
                .crimeTime("15-05-2023 15:47")
                .criminalName("killer")
                .victimName("babka")
                .responsibleDetective("Sherlock Holmes")
                .visionUrl("http://www.youtube.com/2")
                .isCriminalCaught(false)
                .crimeType("INTENTIONAL")
                .placeOfCrime("Village")
                .build();

        List<CrimeCardOutDto> cards = Arrays.asList(cardOutDto,cardOutDto1);
        when(cardService.getAllCards()).thenReturn(cards);
        ResultActions resultActions = mockMvc.perform(get("/api/v1/auditor/cards")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void AuditorController_GetCrimeCard_ReturnsCrimeCardOutDto() throws Exception {
        CrimeCardOutDto cardOutDto = CrimeCardOutDto.builder()
                .id(1L)
                .weapon("weapon")
                .crimeTime("14-07-2023 11:32")
                .criminalName("criminal")
                .victimName("victim")
                .responsibleDetective("Sherlock Holmes")
                .visionUrl("http://www.youtube.com/1")
                .isCriminalCaught(false)
                .crimeType("INTENTIONAL")
                .placeOfCrime("City")
                .build();
        Long cardId = 1L;
        when(cardService.getCardById(cardId)).thenReturn(cardOutDto);
        ResultActions resultActions = mockMvc.perform(get("/api/v1/auditor/cards/{id}", cardId)
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.victimName", CoreMatchers.is(cardOutDto.getVictimName())));
    }

    @Test
    public void AuditorController_ReportMistakeInCard_ReturnsString() throws Exception {
        Long cardId = 1L;
        String message = "Error in card with id=1";
        doNothing().when(cardService).reportCardMistake(cardId, message);
        ResultActions resultActions = mockMvc.perform(post("/api/v1/auditor/cards/{id}", cardId)
                .param("message",message).contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("Report successfully send"));
    }

    @Test
    public void AuditorController_GetUserStatistic_ReturnsUserStatisticInfo() throws Exception {
        Long userId = 1L;
        UserStatisticInfo userStatisticInfo = new UserStatisticInfo();
        UserInfoDto userInfoDto = UserInfoDto.builder()
                .id(1L)
                .login("login")
                .telegramId(15246)
                .firstName("Jack")
                .lastName("Daniels")
                .build();
        userStatisticInfo.setUserInfo(userInfoDto);
        DetectiveStatisticDto detectiveStatisticDto = DetectiveStatisticDto.builder()
                .id(1L)
                .errorsInCards(2)
                .investigationCount(15)
                .build();
        userStatisticInfo.setDetectiveStatistic(Optional.of(detectiveStatisticDto));
        userStatisticInfo.setTechnicStatistic(Optional.empty());
        userStatisticInfo.setBossReactGroupStatistic(Optional.empty());
        when(statisticService.getUserStatistic(userId)).thenReturn(userStatisticInfo);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/auditor/users/{id}", userId).
                contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.userInfo.login", CoreMatchers.is(userStatisticInfo.getUserInfo().getLogin())));
    }
}
