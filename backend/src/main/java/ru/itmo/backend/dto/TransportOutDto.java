package ru.itmo.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransportOutDto {
    private Long id;
    private String brand;
    private String model;
    private int current_fuel;
    private int maximum_fuel;
    private int condition;
    private Boolean status;
}
