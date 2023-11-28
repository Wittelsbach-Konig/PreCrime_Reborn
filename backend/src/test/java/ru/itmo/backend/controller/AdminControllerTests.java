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
import ru.itmo.backend.controllers.AdminController;
import ru.itmo.backend.dto.RegistrationDto;
import ru.itmo.backend.dto.UserOutDto;
import ru.itmo.backend.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@TestPropertySource("/application-test.properties")
public class AdminControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;


    @Test
    public void AdminController_GetAllUsers_ReturnsListOfUserOutDto() throws Exception {
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
        when(userService.getAllUsers()).thenReturn(userList);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/admin/users")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void AdminController_GetUser_ReturnsUserOutDto() throws Exception{
        UserOutDto user1 = UserOutDto.builder()
                .id(1L)
                .login("user1_login")
                .firstName("Username1")
                .lastName("First")
                .telegramId(14521)
                .roles(Arrays.asList("TECHNIC"))
                .build();
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(user1);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/admin/users/1")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Username1"));

    }

    @Test
    public void AdminController_UpdateUser_ReturnsUserOutDto() throws Exception {
        UserOutDto user1 = UserOutDto.builder()
                .id(1L)
                .login("user1_login")
                .firstName("Vasiliy")
                .lastName("Knopkin")
                .telegramId(14521)
                .roles(Arrays.asList("TECHNIC"))
                .build();
        RegistrationDto registrationDto = new RegistrationDto();
        //registrationDto.setId(1L);
        registrationDto.setRoles(Arrays.asList("TECHNIC"));
        registrationDto.setTelegramId(14521);
        registrationDto.setLastName("Knopkin");
        registrationDto.setFirstName("Vasiliy");
        registrationDto.setPassword("security");
        registrationDto.setConfirmPassword("security");
        registrationDto.setLogin("user1_login");

        Long userId = 1L;
        when(userService.updateUser(userId, registrationDto)).thenReturn(user1);

        ResultActions resultActions = mockMvc.perform(put("/api/v1/admin/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDto)).characterEncoding("UTF-8"));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login", CoreMatchers.is(user1.getLogin())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value(user1.getLogin()));
    }

    @Test
    public void AdminController_DeleteUser_ReturnsVoid() throws Exception {
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        ResultActions resultActions = mockMvc.perform(delete("/api/v1/admin/users/1")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());

    }

}
