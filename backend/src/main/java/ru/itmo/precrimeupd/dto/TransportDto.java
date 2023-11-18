package ru.itmo.precrimeupd.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class TransportDto {
    private Long id;
    @NotEmpty
    private String brand;
    @NotEmpty
    private String model;
    @NotEmpty
    private int maximum_fuel;
}
