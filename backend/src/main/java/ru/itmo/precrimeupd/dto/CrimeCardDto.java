package ru.itmo.precrimeupd.dto;

import lombok.Builder;
import lombok.Data;
import ru.itmo.precrimeupd.model.CrimeType;
import ru.itmo.precrimeupd.model.UserEntity;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Builder
public class CrimeCardDto {
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
//    @NotEmpty
//    private UserEntity createdBy;
}
