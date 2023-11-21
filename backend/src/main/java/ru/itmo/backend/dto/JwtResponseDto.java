package ru.itmo.backend.dto;

import lombok.Data;

@Data
public class JwtResponseDto {
    private String accessToken;
    private String tokenType = "Bearer ";

    public JwtResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
