package ru.itmo.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
