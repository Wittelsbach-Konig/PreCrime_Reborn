package ru.itmo.backend.dto;

import lombok.Data;

@Data
public class ReactGroupStatisticDto {
    private Long id;
    private int criminalsCaught;
    private int criminalsEscaped;
}
