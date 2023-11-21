package ru.itmo.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.backend.dto.JwtResponseDto;
import ru.itmo.backend.dto.LoginDto;
import ru.itmo.backend.dto.RegistrationDto;
import ru.itmo.backend.dto.UserOutDto;
import ru.itmo.backend.exceptions.AppError;
import ru.itmo.backend.exceptions.NotValidArgumentException;
import ru.itmo.backend.security.CustomUserDetailService;
import ru.itmo.backend.security.JwtTokenUtils;
import ru.itmo.backend.service.UserService;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final CustomUserDetailService customUserDetailService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserService userService, JwtTokenUtils jwtTokenUtils, AuthenticationManager authenticationManager, CustomUserDetailService customUserDetailService) {
        this.userService = userService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
        this.customUserDetailService = customUserDetailService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getLogin(), loginDto.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Wrong login or password"), HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenUtils.generateToken(authentication);
        return ResponseEntity.ok(new JwtResponseDto(token));
    }

    @PostMapping("/registration")
    public ResponseEntity<UserOutDto> register(@Valid @RequestBody RegistrationDto user,
                           BindingResult result) {
        if (user == null) {
            throw new NotValidArgumentException("Registration data is missing");
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(result.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UserOutDto savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }
}
