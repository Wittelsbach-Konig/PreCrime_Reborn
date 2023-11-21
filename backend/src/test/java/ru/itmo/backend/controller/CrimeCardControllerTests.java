package ru.itmo.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.itmo.backend.controllers.CrimeCardController;
import ru.itmo.backend.dto.CrimeCardInDto;
import ru.itmo.backend.dto.CrimeCardOutDto;
import ru.itmo.backend.models.CrimeCard;
import ru.itmo.backend.service.CardService;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

//@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CrimeCardController.class)
@AutoConfigureMockMvc(addFilters = false)

@ExtendWith(MockitoExtension.class)
//@PropertySource("classpath:application-test.properties")
//@ContextConfiguration("classpath:application.properties")
//@ActiveProfiles("test")
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
        given(cardService.createCard(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/api/v1/cards/newcard")
                .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(cardInDto)).characterEncoding("UTF-8"));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.criminalName", CoreMatchers.is(cardOutDto.getCriminalName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.victimName", CoreMatchers.is(cardOutDto.getVictimName())));

    }
}
