package ru.itmo.precrimeupd.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserOutDto {
    private Long id;
    private String login;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private List<String> roles;
    private int telegramId;
}
