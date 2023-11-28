package ru.itmo.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
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
import ru.itmo.backend.controllers.CrimeCardController;
import ru.itmo.backend.dto.CrimeCardInDto;
import ru.itmo.backend.dto.CrimeCardOutDto;
import ru.itmo.backend.models.CrimeCard;
import ru.itmo.backend.service.CardService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

//@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CrimeCardController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@TestPropertySource("/application-test.properties")
public class CrimeCardControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    @Autowired
    private ObjectMapper objectMapper;

    private CrimeCard crimeCard;
    private CrimeCardInDto cardInDto;
    private CrimeCardOutDto cardOutDto;

    @BeforeEach
    void init(){
        cardInDto = CrimeCardInDto.builder()
                .criminalName("criminal")
                .victimName("victim")
                .weapon("weapon")
                .crimeTime(LocalDateTime.now())
                .visionId(1L)
                .crimeType("INTENTIONAL")
                .placeOfCrime("City")
                .build();
        cardOutDto = CrimeCardOutDto.builder()
                .id(1L)
                .weapon("weapon")
                .crimeTime(LocalDateTime.now())
                .criminalName("criminal")
                .victimName("victim")
                .responsibleDetective("Sherlock Holmes")
                .visionUrl("http://www.youtube.com/1")
                .isCriminalCaught(false)
                .crimeType("INTENTIONAL")
                .placeOfCrime("City")
                .build();
    }

    @Test
    public void CrimeCardController_CreateCard_ReturnCreated() throws Exception{
        when(cardService.createCard(Mockito.any(CrimeCardInDto.class))).thenReturn(cardOutDto);

        ResultActions response = mockMvc.perform(post("/api/v1/cards/newcard")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardInDto)).characterEncoding("UTF-8"));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.criminalName", CoreMatchers.is(cardOutDto.getCriminalName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.victimName", CoreMatchers.is(cardOutDto.getVictimName())))
                .andExpect(MockMvcResultMatchers.jsonPath(("$.victimName")).value(cardInDto.getVictimName()));
    }

    @Test
    public void CrimeCardController_GetCard_ReturnsCrimeCardOutDto() throws  Exception{
        Long cardId = 1L;
        when(cardService.getCardById(cardId)).thenReturn(cardOutDto);
        ResultActions response = mockMvc.perform(get("/api/v1/cards/{id}", cardId)
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void CrimeCardController_GetAllCards_ReturnListCrimeCards() throws Exception{
        CrimeCardOutDto cardOutDto1 = CrimeCardOutDto.builder()
                .id(2L)
                .weapon("knife")
                .crimeTime(LocalDateTime.now())
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
        ResultActions response = mockMvc.perform(get("/api/v1/cards/").contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void CrimeCardController_GetRandomDateTime_ReturnsString() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/v1/cards/randomDateTime")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"));

        // Expected date format
        String dateTimePattern = "\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}:\\d{2}";
        resultActions.andExpect(MockMvcResultMatchers.content().string(Matchers.matchesPattern(dateTimePattern)));
    }

    @Test
    public void CrimeCardController_GetRandomVictimName_ReturnsString() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/v1/cards/randomVictimName")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers
                        .oneOf("Sergey", "Bill", "Alex", "Jane",
                                "Alice", "Diana", "Elizabeth", "Clara", "Jack")));
    }

    @Test
    public void CrimeCardController_GetRandomCriminalName_ReturnsString() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/v1/cards/randomCriminalName")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers
                        .oneOf("John", "Emma", "Michael", "Sophia",
                                "William", "Adolph", "Stepan", "Roman")));
    }

    @Test
    public void CrimeCardController_UpdateCard_ReturnCardDto() throws Exception {
        Long cardId = 1L;
        when(cardService.updateCard(cardId, cardInDto)).thenReturn(cardOutDto);
        ResultActions response = mockMvc.perform(put("/api/v1/cards/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardInDto)).characterEncoding("UTF-8"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.criminalName", CoreMatchers.is(cardOutDto.getCriminalName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.victimName", CoreMatchers.is(cardOutDto.getVictimName())));
    }

    @Test
    public void CrimeCardController_DeleteCard_ReturnsString() throws Exception {
        Long cardId = 1L;
        doNothing().when(cardService).deleteCard(cardId);
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/cards/{id}",cardId)
                                             .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
