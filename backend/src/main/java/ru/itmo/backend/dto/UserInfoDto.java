package ru.itmo.backend.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class UserInfoDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String login;
    private String email;
    private int telegramId;
}
