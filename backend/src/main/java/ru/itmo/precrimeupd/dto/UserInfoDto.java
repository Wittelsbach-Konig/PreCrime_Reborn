package ru.itmo.precrimeupd.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class UserInfoDto {
    private Long id;
    @NotEmpty
    private String firstName;
    private String lastName;
    @NotEmpty
    private String login;
    private String email;
    @NotEmpty
    private int telegramId;
}
