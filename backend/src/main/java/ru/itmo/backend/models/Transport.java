package ru.itmo.backend.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    private String brand;
    private String model;
    private int remaining_fuel;
    private int maximum_fuel;
    @Builder.Default
    private int condition = 100;
    @Builder.Default
    private Boolean inOperation = true;
}
