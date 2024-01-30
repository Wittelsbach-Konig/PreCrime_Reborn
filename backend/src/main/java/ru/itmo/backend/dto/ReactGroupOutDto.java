package ru.itmo.backend.dto;

import lombok.Data;

@Data
public class ReactGroupOutDto {
    private Long id;
    private String memberName;
    private int telegramId;
    private boolean inOperation;
}
