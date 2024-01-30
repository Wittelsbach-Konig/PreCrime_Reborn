package ru.itmo.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrimeCardInDto {
    private Long id;
    @NotEmpty
    @NotNull
    @Size(max = 75)
    private String victimName;

    @NotEmpty
    @NotNull
    @Size(max = 75)
    private String criminalName;

    @NotEmpty
    @NotNull
    @Size(max = 75)
    private String placeOfCrime;

    @NotEmpty
    @NotNull
    @Size(max = 50)
    private String weapon;
    //@NotEmpty
    private LocalDateTime crimeTime;
    @NotEmpty
    @NotNull
    private String crimeType;
    //@NotEmpty
    @NotNull
    private Long visionId;
}
