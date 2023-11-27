package ru.itmo.backend.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "transport")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @NotNull
    @Size(max = 30)
    private String brand;

    @NotEmpty
    @NotNull
    @Size(max = 30)
    private String model;

    private int remaining_fuel;
    private int maximum_fuel;
    @Builder.Default
    private int condition = 100;
    @Builder.Default
    private Boolean inOperation = true;
}
