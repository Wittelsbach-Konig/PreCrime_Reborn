package ru.itmo.backend.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PreCogDto {
    private Long id;
    @NotEmpty
    private String preCogName;
    @NotEmpty
    private int age;
}
