package ru.itmo.backend.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PreCogDto {
    private Long id;
    @NotEmpty
    @NotNull
    @Size(max = 75)
    private String preCogName;
    private int age;
}
