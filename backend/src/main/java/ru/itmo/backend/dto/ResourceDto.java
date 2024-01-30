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
public class ResourceDto {
    private Long id;

    @NotNull
    @NotEmpty
    @Size(max = 30)
    private String resourceName;
    private int amount;
    private int maxPossibleAmount;

    @NotEmpty
    @NotNull
    @Size(max = 30)
    private String type;
}
