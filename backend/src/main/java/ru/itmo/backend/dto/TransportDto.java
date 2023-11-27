package ru.itmo.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransportDto {
    private Long id;

    @NotEmpty
    @NotNull
    @Size(max = 30)
    private String brand;

    @NotEmpty
    @NotNull
    @Size(max = 30)
    private String model;

    private int maximum_fuel;
}
