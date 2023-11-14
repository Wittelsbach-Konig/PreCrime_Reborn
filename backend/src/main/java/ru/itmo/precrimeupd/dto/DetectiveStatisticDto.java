package ru.itmo.precrimeupd.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetectiveStatisticDto {
    private Long id;
    private int investigationCount;
    private int errorsInCards;
}