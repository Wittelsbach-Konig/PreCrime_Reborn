package ru.itmo.precrimeupd.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class TransportDto {
    private Long id;
    @NotEmpty
    private String brand;
    @NotEmpty
    private String model;
    @NotEmpty
    private int maximum_fuel;
}
