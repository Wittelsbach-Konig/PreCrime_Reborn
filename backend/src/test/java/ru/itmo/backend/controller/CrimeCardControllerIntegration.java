package ru.itmo.backend.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.itmo.backend.dto.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/create-roles.sql")
@TestPropertySource("/application-test.properties")

public class CrimeCardControllerIntegration {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    private static Boolean initialized = false;

    @BeforeEach
    public void init(){
        if(initialized){return;}
        initialized = true;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin("Sherl");
        registrationDto.setPassword("pass");
        registrationDto.setConfirmPassword("pass");
        registrationDto.setFirstName("Sherlock");
        registrationDto.setLastName("Holmes");
        List<String> roles = new ArrayList<>();
        roles.add("DETECTIVE");
        registrationDto.setRoles(roles);
        registrationDto.setTelegramId(14528);

        restTemplate.postForEntity("/api/v1/auth/registration", registrationDto, UserOutDto.class);
    }


    @Test
    public void registrateUser() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        RegistrationDto registrationDto1 = new RegistrationDto();
        registrationDto1.setLogin("under");
        registrationDto1.setPassword("password");
        registrationDto1.setConfirmPassword("password");
        registrationDto1.setFirstName("John");
        registrationDto1.setLastName("Undertone");
        List<String> roles = new ArrayList<>();
        roles.add("DETECTIVE");
        registrationDto1.setRoles(roles);
        registrationDto1.setTelegramId(14728);

        ResponseEntity<UserOutDto> registrationResponse = restTemplate.postForEntity("/api/v1/auth/registration", registrationDto1, UserOutDto.class);
        Assertions.assertEquals(HttpStatus.OK, registrationResponse.getStatusCode());
    }


    public String authorizeUser(){
        LoginDto loginDto = new LoginDto();
        loginDto.setLogin("Sherl");
        loginDto.setPassword("pass");
        ResponseEntity<JwtResponseDto> loginResponse = restTemplate.postForEntity("/api/v1/auth/login", loginDto, JwtResponseDto.class);
        return loginResponse.getBody().getAccessToken();
    }

    @Test
    @Sql(value = {"/create-visions.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void CrimeCardController_AddNewCard(){
        String JWTtoken = authorizeUser();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(JWTtoken);

        CrimeCardInDto cardInDto = CrimeCardInDto.builder()
                .criminalName("criminal")
                .victimName("victim")
                .weapon("weapon")
                .crimeTime(LocalDateTime.now())
                .visionId(1L)
                .crimeType("INTENTIONAL")
                .placeOfCrime("City")
                .build();
        CrimeCardOutDto cardOutDto = CrimeCardOutDto.builder()
                .id(1L)
                .weapon("weapon")
                .crimeTime("LocalDateTime.now()")
                .criminalName("criminal")
                .victimName("victim")
                .responsibleDetective("Sherlock Holmes")
                .visionUrl("http://www.youtube.com/1")
                .isCriminalCaught(false)
                .crimeType("INTENTIONAL")
                .placeOfCrime("City")
                .build();
        HttpEntity<CrimeCardInDto> request = new HttpEntity<>(cardInDto, headers);
        ResponseEntity<CrimeCardOutDto> response = restTemplate.postForEntity("/api/v1/cards/newcard", request, CrimeCardOutDto.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(cardOutDto.getCriminalName(),response.getBody().getCriminalName());
    }

    @Test
    public void CrimeCardController_UpdateCard(){
        String JWTtoken = authorizeUser();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(JWTtoken);

        CrimeCardInDto cardInDto = CrimeCardInDto.builder()
                .criminalName("Killer")
                .victimName("woman")
                .weapon("Gun")
                .crimeTime(LocalDateTime.now())
                .visionId(1L)
                .crimeType("INTENTIONAL")
                .placeOfCrime("Village")
                .build();
        Long cardId = 1L;
        HttpEntity<CrimeCardInDto> request = new HttpEntity<>(cardInDto, headers);
        //ResponseEntity<CrimeCardOutDto> response = restTemplate.putForEntity("/api/v1/cards/newcard", request, CrimeCardOutDto.class);
        ResponseEntity<CrimeCardOutDto> response = restTemplate.exchange("/api/v1/cards/{id}", HttpMethod.PUT, request, CrimeCardOutDto.class, cardId);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(cardInDto.getCriminalName(), response.getBody().getCriminalName());
    }

}
