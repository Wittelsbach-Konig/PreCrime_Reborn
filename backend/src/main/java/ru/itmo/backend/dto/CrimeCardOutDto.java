package ru.itmo.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrimeCardOutDto {
    private Long id;
    private String victimName;
    private String criminalName;
    private String placeOfCrime;
    private String weapon;
    private String crimeTime;
    private String crimeType;
    private String responsibleDetective;
    private Boolean isCriminalCaught;
    private String visionUrl;
    private String creationDate;
}
