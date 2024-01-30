package ru.itmo.backend.dto;

import lombok.Data;

@Data
public class ReactGroupStatisticDto {
    private Long id;

    private String memberName;

    private int telegramId;

    private boolean inOperation;

    private int criminalsCaught;

    private int criminalsEscaped;
}
