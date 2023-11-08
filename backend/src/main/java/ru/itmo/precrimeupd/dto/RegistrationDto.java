package ru.itmo.precrimeupd.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class RegistrationDto {
    private Long id;
    @NotEmpty
    private String login;
    @NotEmpty
    private String password;
    private String email;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    private String address;
    @NotEmpty
    private List<String> roles;
    private int telegramId;
}
