package ru.itmo.precrimeupd.dto;


import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ResourceDto {
    private Long id;
    @NotEmpty
    private String resourceName;
    @NotEmpty
    private int amount;
    @NotEmpty
    private int maxPossibleAmount;
    @NotEmpty
    private String type;
}
