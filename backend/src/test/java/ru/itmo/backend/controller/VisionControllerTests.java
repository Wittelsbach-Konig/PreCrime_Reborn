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
import ru.itmo.backend.controllers.VisionController;
import ru.itmo.backend.dto.VisionDto;
import ru.itmo.backend.dto.VisionOutDto;
import ru.itmo.backend.models.Vision;
import ru.itmo.backend.service.VisionService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = VisionController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@TestPropertySource("/application-test.properties")
public class VisionControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VisionService visionService;

    @Test
    public void VisionController_GetVisionList_ReturnsListVision() throws Exception {
        Vision vision1 = Vision.builder()
                .id(1L)
                .videoUrl("http://www.youtube.com/1")
                .accepted(false)
                .build();
        Vision vision2 = Vision.builder()
                .id(2L)
                .videoUrl("http://www.youtube.com/2")
                .accepted(false)
                .build();
        Vision vision3 = Vision.builder()
                .id(3L)
                .videoUrl("http://www.youtube.com/3")
                .accepted(true)
                .build();
        List<Vision> visionList = Arrays.asList(vision1, vision2, vision3);
        when(visionService.getVisionsList()).thenReturn(visionList);
        ResultActions resultActions = mockMvc.perform(get("/api/v1/visions")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void VisionController_AcceptVision_ReturnsString() throws Exception{
        Long visionId = 1L;
        doNothing().when(visionService).approveVision(visionId);
        ResultActions resultActions = mockMvc.perform(post("/api/v1/visions/{id}/accept", visionId)
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("Vision successfully accepted"));
    }

    @Test
    public void VisionController_AddNewVision_ReturnsVisionOutDto() throws Exception{
        VisionDto visionToAdd = VisionDto.builder()
                .videoUrl("http://www.youtube.com/4")
                .build();
        VisionOutDto vision4 = VisionOutDto.builder()
                .id(2L)
                .videoUrl("http://www.youtube.com/4")
                .accepted(false)
                .build();
        when(visionService.saveVision(Mockito.any(VisionDto.class))).thenReturn(vision4);
        ResultActions resultActions = mockMvc.perform(post("/api/v1/visions/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(visionToAdd)).characterEncoding("UTF-8"));
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.videoUrl", CoreMatchers.is(vision4.getVideoUrl())));
    }

    @Test
    public void VisionController_DeleteVision_ReturnsString() throws Exception {
        Long visionId = 1L;
        doNothing().when(visionService).deleteVision(visionId);
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/visions/{id}", visionId));
        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("Vision successfully deleted"));
    }
}
