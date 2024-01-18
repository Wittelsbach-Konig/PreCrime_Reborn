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
import ru.itmo.backend.controllers.PreCogController;
import ru.itmo.backend.dto.PreCogDto;
import ru.itmo.backend.dto.PreCogOutDto;
import ru.itmo.backend.models.PreCog;
import ru.itmo.backend.service.PreCogService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = PreCogController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@TestPropertySource("/application-test.properties")
public class PreCogControllerUnitTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PreCogService preCogService;


    @Test
    public void PreCogController_GetAllPreCogs_ReturnsListOfPreCogs() throws Exception{
        PreCogOutDto preCog1 = PreCogOutDto.builder()
                .age(23)
                .commissionedOn("LocalDateTime.now()")
                .preCogName("Alex")
                .dopamineLevel(100)
                .isWork(true)
                .serotoninLevel(100)
                .stressLevel(0)
                .build();
        PreCogOutDto preCog2 = PreCogOutDto.builder()
                .age(22)
                .commissionedOn("LocalDateTime.now()")
                .preCogName("Mike")
                .dopamineLevel(100)
                .isWork(true)
                .serotoninLevel(100)
                .stressLevel(0)
                .build();
        PreCogOutDto preCog3 = PreCogOutDto.builder()
                .age(23)
                .commissionedOn("LocalDateTime.now()")
                .preCogName("Claire")
                .dopamineLevel(100)
                .isWork(true)
                .serotoninLevel(100)
                .stressLevel(0)
                .build();
        List<PreCogOutDto> preCogs = Arrays.asList(preCog1, preCog2, preCog3);
        when(preCogService.getAllPreCogs()).thenReturn(preCogs);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/precogs")
                .contentType(MediaType.APPLICATION_JSON));


        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void PreCogController_GetAllPreCogs_ReturnsNoContent() throws Exception{

        List<PreCogOutDto> preCogs = new ArrayList<>();
        when(preCogService.getAllPreCogs()).thenReturn(preCogs);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/precogs")
                .contentType(MediaType.APPLICATION_JSON));


        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    public void PreCogController_GetPreCog_ReturnsPreCog() throws Exception {
        PreCogOutDto preCog1 = PreCogOutDto.builder()
                .age(23)
                .commissionedOn("LocalDateTime.now()")
                .preCogName("Alex")
                .dopamineLevel(100)
                .isWork(true)
                .serotoninLevel(100)
                .stressLevel(0)
                .build();
        Long preCogId = 1L;
        when(preCogService.getPreCog(preCogId)).thenReturn(preCog1);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/precogs/1")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.preCogName", CoreMatchers.is(preCog1.getPreCogName())));
    }

    @Test
    public void PreCogController_AddNewPreCog_ReturnsPreCog() throws Exception {
        PreCogDto preCogDto = new PreCogDto();
        preCogDto.setPreCogName("Michael");
        preCogDto.setAge(25);
        PreCogOutDto preCog1 = PreCogOutDto.builder()
                .age(25)
                .commissionedOn("LocalDateTime.now()")
                .preCogName("Michael")
                .dopamineLevel(100)
                .isWork(true)
                .serotoninLevel(100)
                .stressLevel(0)
                .build();

        when(preCogService.addNewPreCog(Mockito.any(PreCogDto.class))).thenReturn(preCog1);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/precogs/newprecog")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(preCogDto)));
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.preCogName", CoreMatchers.is(preCog1.getPreCogName())));
    }

    @Test
    public void PreCogController_UpdatePreCogInfo_ReturnsPreCog() throws Exception {
        PreCogDto preCogDto = new PreCogDto();
        preCogDto.setPreCogName("Michael");
        preCogDto.setAge(25);
        PreCogOutDto preCog1 = PreCogOutDto.builder()
                .age(25)
                .commissionedOn("LocalDateTime.now()")
                .preCogName("Michael")
                .dopamineLevel(100)
                .isWork(true)
                .serotoninLevel(100)
                .stressLevel(0)
                .build();
        Long preCogId = 1L;
        when(preCogService.updatePreCogInfo(preCogId,preCogDto)).thenReturn(preCog1);

        ResultActions resultActions = mockMvc.perform(put("/api/v1/precogs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(preCogDto)));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(preCogDto.getAge()));
    }

    @Test
    public void PreCogController_EnterDopamine_ReturnsString() throws Exception {
        Long preCogId = 1L;
        int amount = 10;
        doNothing().when(preCogService).enterDopamine(preCogId,amount);
        ResultActions resultActions = mockMvc.perform(put("/api/v1/precogs/1/enterdopamine")
                .param("amount", String.valueOf(amount)));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Dopamine successfully added"));
    }

    @Test
    public void PreCogController_EnterSerotonine_ReturnsString() throws Exception {
        Long preCogId = 1L;
        int amount = 10;
        doNothing().when(preCogService).enterSerotonine(preCogId, amount);
        ResultActions resultActions = mockMvc.perform(put("/api/v1/precogs/{id}/enterserotonin", preCogId)
                .param("amount", String.valueOf(amount)));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Serotonine successfully added"));
    }

    @Test
    public void PreCogController_EnterDepressant_ReturnsString() throws Exception {
        Long preCogId = 1L;
        int amount = 10;
        doNothing().when(preCogService).enterDepressant(preCogId, amount);
        ResultActions resultActions = mockMvc.perform(put("/api/v1/precogs/{id}/enterdepressant", preCogId)
                .param("amount", String.valueOf(amount)));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Depressant successfully added"));
    }

    @Test
    public void PreCogController_RetirePreCog_ReturnsPreCog() throws Exception {
        PreCogOutDto preCog1 = PreCogOutDto.builder()
                .age(25)
                .commissionedOn("LocalDateTime.now()")
                .preCogName("Michael")
                .dopamineLevel(100)
                .isWork(false)
                .serotoninLevel(100)
                .stressLevel(0)
                .build();
        Long preCogId = 1L;
        when(preCogService.retirePreCog(preCogId)).thenReturn(preCog1);
        ResultActions resultActions = mockMvc.perform(post("/api/v1/precogs/1/retire"));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.work").value(false));
    }

    @Test
    public void PreCogController_RehabilitatePreCog_ReturnsPreCog() throws Exception {
        PreCogOutDto preCog1 = PreCogOutDto.builder()
                .age(25)
                .commissionedOn("LocalDateTime.now()")
                .preCogName("Michael")
                .dopamineLevel(100)
                .isWork(true)
                .serotoninLevel(100)
                .stressLevel(0)
                .build();
        Long preCogId = 1L;
        when(preCogService.rehabilitatePreCog(preCogId)).thenReturn(preCog1);
        ResultActions resultActions = mockMvc.perform(post("/api/v1/precogs/1/rehabilitate"));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.work").value(true));
    }

    @Test
    public void PreCogController_DeletePreCog_ReturnsString() throws Exception {
        Long preCogId = 1L;
        doNothing().when(preCogService).deletePreCog(preCogId);
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/precogs/1"));
        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().string("PreCog successfully deleted"));
    }
}
