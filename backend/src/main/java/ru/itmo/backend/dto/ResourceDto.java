package ru.itmo.backend.dto;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
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
