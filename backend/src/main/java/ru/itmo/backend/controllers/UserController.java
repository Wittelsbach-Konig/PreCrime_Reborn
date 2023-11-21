package ru.itmo.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.backend.dto.UserOutDto;
import ru.itmo.backend.models.SystemInfo;
import ru.itmo.backend.models.UserEntity;
import ru.itmo.backend.security.SecurityUtil;
import ru.itmo.backend.service.UserService;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class UserController {

    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final SecurityUtil securityUtil;

    @GetMapping("/me")
    public ResponseEntity<UserOutDto> getCurrentUser() {
        String login = securityUtil.getSessionUser();
        UserOutDto outUser = userService.getUserByLogin(login);
        return ResponseEntity.ok(outUser);
    }

    @GetMapping("/credits")
    public ResponseEntity<String> getSystemInfo() throws JsonProcessingException {
        SystemInfo info = new SystemInfo();
        String jsonInfo = objectMapper.writeValueAsString(info);
        return ResponseEntity.ok().body(jsonInfo);
    }
}
