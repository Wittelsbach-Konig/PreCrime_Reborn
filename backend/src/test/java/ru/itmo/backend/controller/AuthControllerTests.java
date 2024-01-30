package ru.itmo.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.itmo.backend.controllers.AuthController;
import ru.itmo.backend.dto.LoginDto;
import ru.itmo.backend.dto.RegistrationDto;
import ru.itmo.backend.dto.UserOutDto;
import ru.itmo.backend.security.CustomUserDetailService;
import ru.itmo.backend.security.JwtTokenUtils;
import ru.itmo.backend.service.UserService;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@TestPropertySource("/application-test.properties")
public class AuthControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;
    @MockBean
    private CustomUserDetailService customUserDetailService;
    @MockBean
    private JwtTokenUtils jwtTokenUtils;
    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    public void AuthController_Login_Successful() throws Exception {
        String loginUrl = "/api/v1/auth/login";
        LoginDto loginDto = new LoginDto();
        loginDto.setLogin("testLogin");
        loginDto.setPassword("testPassword");
        String token = "testToken";

        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDto.getLogin(), loginDto.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenUtils.generateToken(authentication)).thenReturn(token);

        ResultActions resultActions = mockMvc.perform(post(loginUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)));

        resultActions.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").value(token));
    }

    @Test
    public void AuthController_Login_Failed() throws Exception {
        String loginUrl = "/api/v1/auth/login";
        LoginDto loginDto = new LoginDto();
        loginDto.setLogin("testLogin");
        loginDto.setPassword("invalidPassword");
        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);
        ResultActions resultActions = mockMvc.perform(post(loginUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)));
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Wrong login or password"));
    }

    @Test
    public void AuthController_Register_Successful() throws Exception {
        String registerUrl = "/api/v1/auth/registration";
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin("testUser");
        registrationDto.setPassword("testPassword");
        registrationDto.setConfirmPassword("testPassword");
        registrationDto.setFirstName("Jack");
        registrationDto.setLastName("Ripper");
        registrationDto.setRoles(Arrays.asList("DETECTIVE"));

        UserOutDto savedUser = UserOutDto.builder()
                .id(1L)
                .login("testUser")
                .build();

        when(userService.saveUser(any())).thenReturn(savedUser);

        ResultActions resultActions = mockMvc.perform(post(registerUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDto)));

        resultActions.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedUser.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value(savedUser.getLogin()));
    }

    @Test
    public void AuthController_Register_NotAllData_ReturnsBadRequest() throws Exception {
        String registerUrl = "/api/v1/auth/registration";
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin("testUser");
        registrationDto.setPassword("testPassword");
        registrationDto.setConfirmPassword("mismatchedPassword");

        ResultActions resultActions = mockMvc.perform(post(registerUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDto)));

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void AuthController_Register_Failed_ValidationErrors() throws Exception {
        String registerUrl = "/api/v1/auth/registration";
        //RegistrationDto registrationDto = new RegistrationDto();

        RegistrationDto registrationDto = new RegistrationDto("", "pass", "pass",
                null, "firstName", "lastName", Arrays.asList("DETECTIVE"), 15472);

        ResultActions resultActions = mockMvc.perform(post(registerUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDto)));

        resultActions.andExpect(status().isBadRequest());
    }
}
