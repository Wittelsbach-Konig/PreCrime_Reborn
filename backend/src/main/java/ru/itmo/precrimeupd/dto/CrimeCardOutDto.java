package ru.itmo.precrimeupd.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Builder
public class CrimeCardOutDto {
    private Long id;
    @NotEmpty
    private String victimName;
    @NotEmpty
    private String criminalName;
    @NotEmpty
    private String placeOfCrime;
    @NotEmpty
    private String weapon;
    @NotEmpty
    private LocalDateTime crimeTime;
    @NotEmpty
    private String crimeType;
    @NotEmpty
    private String responsibleDetective;
    private Boolean isCriminalCaught;
    private String visionUrl;
}
