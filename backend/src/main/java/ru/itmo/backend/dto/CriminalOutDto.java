package ru.itmo.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CriminalOutDto {
    private Long id;
    private String name;
    private String location;
    private String weapon;
    private String status;
    private Boolean isArrestAssigned;
}
