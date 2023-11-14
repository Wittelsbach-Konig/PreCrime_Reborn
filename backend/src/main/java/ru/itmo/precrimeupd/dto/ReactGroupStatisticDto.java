package ru.itmo.precrimeupd.dto;

import lombok.Data;

@Data
public class ReactGroupStatisticDto {
    private Long id;
    private int criminalsCaught;
    private int criminalsEscaped;
}
